package com.example.paper.service.impl;

import com.example.paper.dao.UserActionRepository;
import com.example.paper.dao.UserInterestRepository;
import com.example.paper.dao.UserRepository;
import com.example.paper.entity.userActionEntity.ClickAction;
import com.example.paper.entity.userActionEntity.PaperCollection;
import com.example.paper.entity.userActionEntity.UserAction;
import com.example.paper.entity.userInterestEntity.PaperInterest;
import com.example.paper.entity.userInterestEntity.UserInterest;
import com.example.paper.entity.vo.ResponseVO;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActionRepository userActionRepository;
    @Autowired
    UserInterestRepository userInterestRepository;

    @Override
    public ResponseVO interestSingleUpdate(Integer uid) {
        Optional<UserAction> userActionOptional=userActionRepository.findById(uid.longValue());

        if(userActionOptional.isPresent()){
            UserAction userAction=userActionOptional.get();
            UserInterest userInterest=new UserInterest(uid);

            List<PaperInterest> paperInterests=new ArrayList<>();

            List<ClickAction> clickActions=userAction.getClickActions();
            List<PaperCollection> paperCollections=userAction.getPaperCollections();

            clickActions.sort(Comparator.comparing(ClickAction::getPaperId));
            paperCollections.sort(Comparator.comparing(PaperCollection::getPaperId));

            int c_index=0,p_index=0;
            int c_size=clickActions.size(),p_size=paperCollections.size();
            while(c_index<c_size&&p_index<p_size){
                ClickAction current_click=clickActions.get(c_index);
                PaperCollection current_paper=paperCollections.get(p_index);
                if(current_click.getPaperId().equals(current_paper.getPaperId())){
                    PaperInterest paperInterest=new PaperInterest(current_click.getPaperId(),current_click.getPaperTitle(),
                            countInterest(current_click,current_paper));
                    c_index++;
                    p_index++;
                    paperInterests.add(paperInterest);
                }
                else if(current_click.getPaperId()<current_paper.getPaperId()){
                    PaperInterest paperInterest=new PaperInterest(current_click.getPaperId(),current_click.getPaperTitle(),
                            countInterest(current_click));
                    c_index++;
                    paperInterests.add(paperInterest);
                }
                else{
                    PaperInterest paperInterest=new PaperInterest(current_paper.getPaperId(),current_paper.getPaperTitle(),
                            countInterest(current_paper));
                    p_index++;
                    paperInterests.add(paperInterest);
                }
            }

            while(c_index<c_size){
                ClickAction current_click=clickActions.get(c_index);
                PaperInterest paperInterest=new PaperInterest(current_click.getPaperId(),current_click.getPaperTitle(),
                        countInterest(current_click));
                c_index++;
                paperInterests.add(paperInterest);
            }

            while(p_index<p_size){
                PaperCollection current_paper=paperCollections.get(p_index);
                PaperInterest paperInterest=new PaperInterest(current_paper.getPaperId(),current_paper.getPaperTitle(),
                        countInterest(current_paper));
                p_index++;
                paperInterests.add(paperInterest);
            }

            normalizeInterest(paperInterests);

            userInterest.setPaperInterests(paperInterests);
            userInterestRepository.save(userInterest);
            return ResponseVO.buildSuccess("更新用户兴趣成功");
        }
        else{
            if(userRepository.getById(uid)==null){
                return ResponseVO.buildFailure("用户不存在");
            }
            UserInterest userInterest=new UserInterest(uid);
            userInterest.setPaperInterests(new ArrayList<PaperInterest>());
            userInterestRepository.save(userInterest);
            return ResponseVO.buildSuccess("用户暂无相关行为");
        }
    }

    //每两个小时定时更新一次全体用户的兴趣度
    @Override
    @Scheduled(cron = "0 0 0/2 * * ?")
    public ResponseVO interestAllUpdate() {
        System.out.println("更新全部用户"+new Date().toString());
        List<Integer> uid_list=userRepository.getAllUserId();
        if(uid_list==null){
            return ResponseVO.buildFailure("系统中暂无用户");
        }
        for(Integer uid:uid_list){
            interestSingleUpdate(uid);
        }
        return ResponseVO.buildSuccess("所有用户兴趣度更新成功");
    }

    //采用最大最小标准化
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
            double normalizedInterest=(paperInterest.getInterest()-min_interest)/baseNorm;
            BigDecimal b = new BigDecimal(normalizedInterest);
            normalizedInterest= b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
            paperInterest.setInterest(normalizedInterest);
        }
    }


    private Double countInterest(ClickAction clickAction,PaperCollection paperCollection){
        double res=countInterest(clickAction)+countInterest(paperCollection);
        BigDecimal b = new BigDecimal(res);
        res= b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();

        return res;
    }

    //计算点击事件贡献的兴趣度
    // ln(click+1) * 2 ^ (-(1/3)(Tc-Tl))
    private Double countInterest(ClickAction clickAction){
        Integer clickCount=clickAction.getClickCount();
        Long lastTime=clickAction.getLastTime();
        Long currentTime=new Date().getTime();
        int days=getBetweenDays(lastTime,currentTime);

        double res=Math.log(clickCount+1)*Math.pow(2,(-1.0/3.0)*days);
        BigDecimal b = new BigDecimal(res);
        res= b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        return res;
    }

    //计算收藏论文贡献的兴趣度
    private Double countInterest(PaperCollection paperCollection){
        Long lastTime= paperCollection.getLastTime();
        Long currentTime=new Date().getTime();
        int days=getBetweenDays(lastTime,currentTime);

        double res=2.0*Math.pow(2,(-1.0/5.0)*days);
        BigDecimal b = new BigDecimal(res);
        res= b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        return res;
    }

    private int getBetweenDays(Long lastTime,Long currentTime){
        return (int)((currentTime-lastTime)/MILLS_DAY);
    }

}
