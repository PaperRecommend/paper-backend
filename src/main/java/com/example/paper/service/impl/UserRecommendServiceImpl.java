package com.example.paper.service.impl;

import com.example.paper.dao.*;
import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.userActionEntity.ClickAction;
import com.example.paper.entity.userActionEntity.PaperCollection;
import com.example.paper.entity.userActionEntity.UserAction;
import com.example.paper.entity.userInterestEntity.PaperInterest;
import com.example.paper.entity.userInterestEntity.UserInterest;
import com.example.paper.entity.userRecommendEntity.PaperRecommend;
import com.example.paper.entity.userRecommendEntity.UserRecommend;
import com.example.paper.entity.userSimilarityEntity.Similarity;
import com.example.paper.entity.userSimilarityEntity.UserSimilarity;
import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.LdaService;
import com.example.paper.service.UserRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
@EnableScheduling

public class UserRecommendServiceImpl implements UserRecommendService {

    private static final int MILLS_DAY=1000*60*60*24;
    private static final int TOP_SIMILAR=40;
    private static final double THRESHOLD_WEIGHT=0.1;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserActionRepository userActionRepository;
    @Autowired
    UserInterestRepository userInterestRepository;
    @Autowired
    UserSimilarityRepository userSimilarityRepository;
    @Autowired
    UserRecommendRepository userRecommendRepository;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    LdaService ldaService;


    @Override
    public ResponseVO interestSingleUpdate(Integer uid) {
        Optional<UserAction> userActionOptional=userActionRepository.findById(uid.longValue());
        //???????????????????????????
        if(userActionOptional.isPresent()){
            UserAction userAction=userActionOptional.get();
            UserInterest userInterest=new UserInterest(uid);
            List<PaperInterest> paperInterests=new ArrayList<>();

            List<ClickAction> clickActions=userAction.getClickActions();
            List<PaperCollection> paperCollections=userAction.getPaperCollections();
            //??????????????????????????????????????????

            clickActions.sort(Comparator.comparing(ClickAction::getPaperId));
            paperCollections.sort(Comparator.comparing(PaperCollection::getPaperId));

            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????????????????????????????
            int c_index=0,p_index=0;
            int c_size=clickActions.size(),p_size=paperCollections.size();
            while(c_index<c_size&&p_index<p_size){
                ClickAction current_click=clickActions.get(c_index);
                PaperCollection current_paper=paperCollections.get(p_index);
                if(current_click.getPaperId().equals(current_paper.getPaperId())){
                    PaperInterest paperInterest=new PaperInterest(current_click.getPaperId(),
                            countInterest(current_click,current_paper));
                    c_index++;
                    p_index++;
                    paperInterests.add(paperInterest);
                }
                else if(current_click.getPaperId()<current_paper.getPaperId()){
                    PaperInterest paperInterest=new PaperInterest(current_click.getPaperId(),
                            countInterest(current_click));
                    c_index++;
                    paperInterests.add(paperInterest);
                }
                else{
                    PaperInterest paperInterest=new PaperInterest(current_paper.getPaperId(),
                            countInterest(current_paper));
                    p_index++;
                    paperInterests.add(paperInterest);
                }
            }

            while(c_index<c_size){
                ClickAction current_click=clickActions.get(c_index);
                PaperInterest paperInterest=new PaperInterest(current_click.getPaperId(),
                        countInterest(current_click));
                c_index++;
                paperInterests.add(paperInterest);
            }
            while(p_index<p_size){
                PaperCollection current_paper=paperCollections.get(p_index);
                PaperInterest paperInterest=new PaperInterest(current_paper.getPaperId(),
                        countInterest(current_paper));
                p_index++;
                paperInterests.add(paperInterest);
            }
            //?????????????????????????????????????????????
            normalizeInterest(paperInterests);

            userInterest.setPaperInterests(paperInterests);
            userInterestRepository.save(userInterest);
            return ResponseVO.buildSuccess("????????????????????????");
        }
        else{
            if(userRepository.getById(uid)==null){
                return ResponseVO.buildFailure("???????????????");
            }
            UserInterest userInterest=new UserInterest(uid);
            userInterest.setPaperInterests(new ArrayList<PaperInterest>());
            userInterestRepository.save(userInterest);
            return ResponseVO.buildSuccess("????????????????????????");
        }
    }

    //?????????????????????????????????????????????????????????
    @Override
    @Scheduled(cron = "0 0 0/2 * * ?")
    public ResponseVO interestAllUpdate() {
        List<Integer> uid_list=userRepository.getAllUserId();
        if(uid_list==null){
            return ResponseVO.buildFailure("?????????????????????");
        }
        for(Integer uid:uid_list){
            interestSingleUpdate(uid);
        }
        return ResponseVO.buildSuccess("?????????????????????????????????");
    }

    @Override
    public ResponseVO singleUserSimilarity(Integer uid) {
        Optional<UserInterest> userInterestOptional=userInterestRepository.findById(uid.longValue());
        if(userInterestOptional.isPresent()){
            UserInterest userInterest=userInterestOptional.get();
            List<PaperInterest> paperInterests=userInterest.getPaperInterests();
            paperInterests.sort(Comparator.comparing(PaperInterest::getPaperId));

            List<Similarity> similarities=new ArrayList<>();

            Iterator<UserInterest> userInterestIterator=userInterestRepository.findAll().iterator();
            List<UserInterest> userInterests=copyIterator(userInterestIterator);
            iterateSimilarity(userInterests, userInterest, paperInterests, similarities);
            //???????????????????????????????????????
            UserSimilarity userSimilarity=new UserSimilarity(uid);
            userSimilarity.setSimilarities(similarities);

            userSimilarityRepository.save(userSimilarity);
            return ResponseVO.buildSuccess("???????????????????????????");
        }
        else {
            UserSimilarity userSimilarity=new UserSimilarity(uid);
            userSimilarity.setSimilarities(new ArrayList<>());
            userSimilarityRepository.save(userSimilarity);
            return ResponseVO.buildSuccess("??????????????????????????????");
        }
    }

    //???????????????????????????????????????
    @Override
    @Scheduled(cron = "0 0 0/2 * * ?")
    public ResponseVO allUserSimilarity() {
        List<Integer> uid_list=userRepository.getAllUserId();
        Iterator<UserInterest> userInterestIterator=userInterestRepository.findAll().iterator();
        List<UserInterest> userInterests=copyIterator(userInterestIterator);
        for(Integer uid:uid_list){
            Optional<UserInterest> userInterestOptional=userInterestRepository.findById(uid.longValue());
            if(userInterestOptional.isPresent()){
                UserInterest userInterest=userInterestOptional.get();
                List<PaperInterest> paperInterests=userInterest.getPaperInterests();
                paperInterests.sort(Comparator.comparing(PaperInterest::getPaperId));

                List<Similarity> similarities=new ArrayList<>();
                iterateSimilarity(userInterests, userInterest, paperInterests, similarities);


                UserSimilarity userSimilarity=new UserSimilarity(uid);
                userSimilarity.setSimilarities(similarities);
                userSimilarityRepository.save(userSimilarity);
            }
            else{
                UserSimilarity userSimilarity=new UserSimilarity(uid);
                userSimilarity.setSimilarities(new ArrayList<>());
                userSimilarityRepository.save(userSimilarity);
            }
        }
        return ResponseVO.buildSuccess("????????????????????????????????????");
    }

    @Override
    public ResponseVO recommendSingleUpdate(Integer uid) {
        Optional<UserSimilarity> userSimilarityOptional=userSimilarityRepository.findById(uid.longValue());
        if(userSimilarityOptional.isPresent()){
            UserSimilarity userSimilarity=userSimilarityOptional.get();
            List<Similarity> similarities=userSimilarity.getSimilarities();
            similarities.sort(Comparator.comparing(Similarity::getSimilarity).reversed());

            if(similarities.size()>TOP_SIMILAR){
                similarities=similarities.subList(0,TOP_SIMILAR);
            }

            //??????????????????????????????????????????
            List<UserInterest> userInterests=new ArrayList<>();
            for(Similarity similarity:similarities){
                userInterests.add(userInterestRepository.findById(similarity.getUid().longValue()).get());
            }

            //??????????????????????????????????????????????????????
            Set<Long> paperIdSet=new HashSet<>();
            for(UserInterest userInterest:userInterests){
                for(PaperInterest paperInterest:userInterest.getPaperInterests()){
                    paperIdSet.add(paperInterest.getPaperId());
                }
            }

            //????????????????????????id???????????????
            List<Long> paperIds=new ArrayList<>(paperIdSet);
            paperIds.sort(Comparator.comparing(Long::longValue));

            //??????????????????(????????????????????????0)
            for(UserInterest userInterest:userInterests){
                List<PaperInterest> paperInterests=userInterest.getPaperInterests();
                paperInterests.sort(Comparator.comparing(PaperInterest::getPaperId));
                List<PaperInterest> newPaperInterest=new ArrayList<>();

                int paper_interest_index=0;
                for(Long paperId:paperIds){
                    if(paper_interest_index<paperInterests.size()){
                        if(paperInterests.get(paper_interest_index).getPaperId()>paperId){
                            newPaperInterest.add(new PaperInterest(paperId,0.0));
                        }
                        else{
                            newPaperInterest.add(paperInterests.get(paper_interest_index++));
                        }
                    }
                    else{
                        newPaperInterest.add(new PaperInterest(paperId,0.0));
                    }
                }
                userInterest.setPaperInterests(newPaperInterest);
            }

            List<PaperRecommend> paperRecommendList=new ArrayList<>();
            for(Long paperId:paperIds){
                paperRecommendList.add(new PaperRecommend(paperId,0.0));
            }

            for(int i=0;i<similarities.size();i++){
                Similarity similarity=similarities.get(i);
                UserInterest userInterest=userInterests.get(i);
                for(int j=0;j<paperIds.size();j++){
                    double weight=paperRecommendList.get(j).getWeight();
                    paperRecommendList.get(j).setWeight(weight+(userInterest.getPaperInterests().get(j).getInterest()*similarity.getSimilarity()));
                }
            }
            //???????????????????????????????????????????????????
            paperRecommendList.sort(Comparator.comparing(PaperRecommend::getWeight).reversed());

            int threshold_idx=paperRecommendList.size();
            for(int i=0;i<paperRecommendList.size();i++){
                if(paperRecommendList.get(i).getWeight()<THRESHOLD_WEIGHT){
                    threshold_idx=i;
                    break;
                }
            }
            paperRecommendList=paperRecommendList.subList(0,threshold_idx);

            //?????????????????????????????????????????????????????????
            List<PaperCollection> paperCollections=userActionRepository.findById(uid.longValue()).get().getPaperCollections();
            List<ClickAction> clickActions=userActionRepository.findById(uid.longValue()).get().getClickActions();
            Set<Long> actionPaperIdSet=new HashSet<>();
            for(PaperCollection paperCollection:paperCollections){
                actionPaperIdSet.add(paperCollection.getPaperId());
            }
            for(ClickAction clickAction:clickActions){
                actionPaperIdSet.add(clickAction.getPaperId());
            }
            List<Long> actionPaperIdList=new ArrayList<>(actionPaperIdSet);
            actionPaperIdList.sort(Comparator.comparing(Long::longValue));
            paperRecommendList.sort(Comparator.comparing(PaperRecommend::getPaperId));

            int index_1=0,index_2=0;
            int size_1=actionPaperIdList.size(),size_2=paperRecommendList.size();
            List<Integer> removeList=new ArrayList<>();

            while(index_1<size_1&&index_2<size_2){
                Long long_1=actionPaperIdList.get(index_1);
                Long long_2=paperRecommendList.get(index_2).getPaperId();
                if(long_1.equals(long_2)){
                    removeList.add(index_2);
                    index_1++;
                    index_2++;
                }
                else if(long_1>long_2){
                    index_2++;
                }
                else{
                    index_1++;
                }
            }
            for(int i=0;i<removeList.size();i++){
                paperRecommendList.remove(removeList.get(i)-i);
            }

            paperRecommendList.sort(Comparator.comparing(PaperRecommend::getWeight).reversed());
            UserRecommend userRecommend=new UserRecommend(uid,paperRecommendList);
            userRecommendRepository.save(userRecommend);
            return ResponseVO.buildSuccess("????????????????????????");
        }
        else{
            return ResponseVO.buildFailure("???????????????????????????");
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public ResponseVO recommendAllUpdate() {
        List<Integer> uid_list=userRepository.getAllUserId();
        for(Integer uid:uid_list){
            recommendSingleUpdate(uid);
        }
        return ResponseVO.buildSuccess("????????????????????????????????????");
    }

    @Override
    public List<PaperRecommend> getUserRecommend(Integer uid, int n) {
        Optional<UserRecommend> userRecommendOptional=userRecommendRepository.findById(uid.longValue());
        if(userRecommendOptional.isPresent()){
            List<PaperRecommend> paperRecommends=userRecommendOptional.get().getPaperRecommendList();
            System.out.println("????????????????????????:"+paperRecommends.size());
            if(paperRecommends.size()>n){
                paperRecommends=paperRecommends.subList(0,n);
            }
            return paperRecommends;
        }
        else{
            return new ArrayList<>();
        }

    }

    @Override
    public List<Paper> mixedRecommend(Integer uid, int size) {
        int cfSize;
        Optional<UserRecommend> userRecommendOptional=userRecommendRepository.findById(uid.longValue());
        List<Long> wholeRecommendList=new ArrayList<>();
        if(userRecommendOptional.isPresent()){
            List<PaperRecommend> paperRecommendList=userRecommendOptional.get().getPaperRecommendList();
            cfSize=Math.min(size*2,paperRecommendList.size());
            for(int i=0;i<cfSize;i++){
                wholeRecommendList.add(paperRecommendList.get(i).getPaperId());
            }
        }
        else{
            cfSize=0;
        }
        int ldaSize=Math.min(5*size-cfSize,60);
        wholeRecommendList.addAll(ldaService.getUserLdaPapers(uid,ldaSize));
        List<Long> recommendList=UserServiceImpl.randomGetPaper(wholeRecommendList,size);
        List<Paper> papers=new ArrayList<>();
        for(Long paperId:recommendList){
            papers.add(paperRepository.findById(paperId).get());
        }
        return papers;
    }

    @Override
    public Double countAverage(Integer begin_uid, Integer end_uid) {
        double sum=0.0;
        for(Integer uid=begin_uid;uid<=end_uid;uid++){
            Optional<UserRecommend> userRecommendOptional=userRecommendRepository.findById(uid.longValue());
            if(userRecommendOptional.isPresent()){
                sum+=userRecommendOptional.get().getPaperRecommendList().size();
            }
        }
        return sum/(end_uid-begin_uid+1);
    }


    //?????????????????????????????????????????????????????????
    private void iterateSimilarity(List<UserInterest> userInterests, UserInterest userInterest, List<PaperInterest> paperInterests, List<Similarity> similarities) {
        for (UserInterest otherUserInterest:userInterests) {
            if(otherUserInterest.getId().equals(userInterest.getId())){
                continue;
            }
            else{
                List<PaperInterest> otherUserPaperInterest=otherUserInterest.getPaperInterests();
                otherUserPaperInterest.sort(Comparator.comparing(PaperInterest::getPaperId));
                double similarity=countSimilarity(paperInterests,otherUserPaperInterest);
                similarities.add(new Similarity(otherUserInterest.getId(),similarity));
            }
        }
    }


    //???????????????????????????
    //Min-Max Normalization
    private void normalizeInterest(List<PaperInterest> paperInterests){
        double min_interest=Double.MAX_VALUE;
        double max_interest=-Double.MAX_VALUE;
        for(PaperInterest paperInterest:paperInterests){
            min_interest=Math.min(min_interest,paperInterest.getInterest());
            max_interest=Math.max(max_interest,paperInterest.getInterest());
        }

        double baseNorm=(max_interest-min_interest);
        for(PaperInterest paperInterest:paperInterests){
            if(baseNorm==0.0){
                paperInterest.setInterest(1.0);
            }
            else {
                double normalizedInterest=doubleRemain((paperInterest.getInterest()-min_interest)/baseNorm,4);
                paperInterest.setInterest(normalizedInterest);
            }
        }
    }


    private Double countInterest(ClickAction clickAction,PaperCollection paperCollection){
        double res=doubleRemain(countInterest(clickAction)+countInterest(paperCollection),4);
        return res;
    }
    //????????????????????????????????????
    // ln(click+1) * 2 ^ (-(1/3)(Tc-Tl))
    private Double countInterest(ClickAction clickAction){
        Integer clickCount=clickAction.getClickCount();
        Long lastTime=clickAction.getLastTime();
        Long currentTime=new Date().getTime();
        int days=getBetweenDays(lastTime,currentTime);

        double res=doubleRemain(Math.log(clickCount+1)*Math.pow(2,(-1.0/3.0)*days),4);
        return res;
    }

    //????????????????????????????????????
    //2*2^(-(1/5)(Tc-Tl))
    private Double countInterest(PaperCollection paperCollection){
        Long lastTime= paperCollection.getLastTime();
        Long currentTime=new Date().getTime();
        int days=getBetweenDays(lastTime,currentTime);

        double res=doubleRemain(2.0*Math.pow(2,(-1.0/5.0)*days),4);
        return res;
    }

    private int getBetweenDays(Long lastTime,Long currentTime){
        return (int)((currentTime-lastTime)/MILLS_DAY);
    }

    private double countSimilarity(List<PaperInterest> paperInterests_1,List<PaperInterest> paperInterests_2){
        int index_1=0,index_2=0;
        int size_1=paperInterests_1.size(),size_2=paperInterests_2.size();
        double up_sum=0.0;
        double pow_sum_1=0.0,pow_sum_2=0.0;
        while(index_1<size_1&&index_2<size_2){
            PaperInterest paperInterest_1=paperInterests_1.get(index_1);
            PaperInterest paperInterest_2=paperInterests_2.get(index_2);
            if(paperInterest_1.getPaperId().equals(paperInterest_2.getPaperId())){
                up_sum+=paperInterest_1.getInterest()*paperInterest_2.getInterest();
                pow_sum_1+=Math.pow(paperInterest_1.getInterest(),2.0);
                pow_sum_2+=Math.pow(paperInterest_2.getInterest(),2.0);
                index_1++;
                index_2++;
            }
            else if(paperInterest_1.getPaperId()<paperInterest_2.getPaperId()){
                pow_sum_1+=Math.pow(paperInterest_1.getInterest(),2.0);
                index_1++;
            }
            else{
                pow_sum_2+=Math.pow(paperInterest_2.getInterest(),2.0);
                index_2++;
            }
        }

        while(index_1<size_1){
            PaperInterest paperInterest_1=paperInterests_1.get(index_1);
            pow_sum_1+=Math.pow(paperInterest_1.getInterest(),2.0);
            index_1++;
        }

        while(index_2<size_2){
            PaperInterest paperInterest_2=paperInterests_2.get(index_2);
            pow_sum_2+=Math.pow(paperInterest_2.getInterest(),2.0);
            index_2++;
        }
        if(pow_sum_1*pow_sum_2==0.0){
            return 0.0;
        }
        double res=up_sum/(Math.pow(pow_sum_1,0.5)*Math.pow(pow_sum_2,0.5));
        return doubleRemain(res,4);
    }

    private double doubleRemain(double res,int scale){
        BigDecimal b=new BigDecimal(res);
        return b.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    private <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

}
