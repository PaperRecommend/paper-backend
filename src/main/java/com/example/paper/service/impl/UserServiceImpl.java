package com.example.paper.service.impl;

import com.example.paper.config.security.JwtTokenUtils;
import com.example.paper.dao.PaperRepository;
import com.example.paper.dao.UserActionRepository;
import com.example.paper.dao.UserRepository;
import com.example.paper.dao.UserSearchRepository;
import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.po.UserPO;
import com.example.paper.entity.userActionEntity.ClickAction;
import com.example.paper.entity.userActionEntity.PaperCollection;
import com.example.paper.entity.userActionEntity.UserAction;
import com.example.paper.entity.userSearchEntity.SearchAction;
import com.example.paper.entity.userSearchEntity.UserSearch;
import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.PaperService;
import com.example.paper.service.UserService;

import java.util.*;

import com.example.paper.utils.CookieUtils;
import com.example.paper.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActionRepository userActionRepository;

    @Autowired
    UserSearchRepository userSearchRepository;

    @Autowired
    CookieUtils cookieUtils;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    PaperService paperService;

    @Override
    public int getUserCount() {
        return (int)userRepository.count();
    }

    @Override
    public ResponseVO registerUser(String name, String password) {
        if(userRepository.getByName(name)!=null){
            return ResponseVO.buildFailure("用户名已存在");
        }
        //加密后存入数据库
        userRepository.save(new UserPO(name,md5(password)));
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO login(String name, String password, HttpServletRequest request,
                            HttpServletResponse response) {
        UserPO userPO=userRepository.getByName(name);

        if(userPO==null){
            return ResponseVO.buildFailure("用户名不存在");
        }
        if(userPO.getPassword().equals(md5(password))){
            cookieUtils.set(response,"uid",userPO.getId().toString());
            return ResponseVO.buildSuccess(JwtTokenUtils.generateToken(userPO));
        }
        else{
            return ResponseVO.buildFailure("密码错误");
        }
    }
    @Override
    public UserPO getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public Integer getIdByUsername(String username) {
        UserPO userPO=userRepository.getByName(username);
        return userPO==null?-1:userPO.getId();
    }

    @Override
    public ResponseVO clickAction(Integer uid, Long paperId) {
        Optional<UserAction> userInterestOptional= userActionRepository.findById(uid.longValue());
        UserAction userAction;
        if(userInterestOptional.isPresent()){
            userAction =userInterestOptional.get();
            List<ClickAction> clickActions= userAction.getClickActions();

            boolean hasClickedBefore=false;
            for(ClickAction clickAction:clickActions){
                if(clickAction.getPaperId().equals(paperId)){
                    clickAction.setClickCount(clickAction.getClickCount()+1);
                    //更新最后一次click时间
                    clickAction.setLastTime(new Date().getTime());
                    hasClickedBefore=true;
                    break;
                }
            }
            if(!hasClickedBefore){
                clickActions.add(new ClickAction(paperId,1,new Date().getTime()));
            }
        }
        else{
            userAction =new UserAction(uid);
            List<ClickAction> clickActions=new ArrayList<>();
            clickActions.add(new ClickAction(paperId,1,new Date().getTime()));
            List<PaperCollection> paperCollections=new ArrayList<>();
            userAction.setPaperCollections(paperCollections);
            userAction.setClickActions(clickActions);
        }
        userActionRepository.save(userAction);
        return ResponseVO.buildSuccess("点击记录成功");
    }

    @Override
    public ResponseVO collectPaper(Integer uid, Long paperId) {
        Optional<UserAction> userInterestOptional= userActionRepository.findById(uid.longValue());
        UserAction userAction;
        if(userInterestOptional.isPresent()){
            userAction =userInterestOptional.get();
            List<PaperCollection> paperCollections= userAction.getPaperCollections();

            for(PaperCollection paperCollection:paperCollections){
                if(paperCollection.getPaperId().equals(paperId)) {
                    return ResponseVO.buildFailure("已经收藏过了");
                }
            }
            paperCollections.add(new PaperCollection(paperId,new Date().getTime()));
        }
        else{
            userAction =new UserAction(uid);
            List<PaperCollection> paperCollections=new ArrayList<>();
            paperCollections.add(new PaperCollection(paperId,new Date().getTime()));
            List<ClickAction> clickActions=new ArrayList<>();
            userAction.setClickActions(clickActions);
            userAction.setPaperCollections(paperCollections);
        }
        userActionRepository.save(userAction);
        return ResponseVO.buildSuccess("收藏成功");
    }

    @Override
    public ResponseVO cancelCollection(Integer uid, Long paperId) {
        Optional<UserAction> userInterestOptional= userActionRepository.findById(uid.longValue());
        if(userInterestOptional.isPresent()){
            UserAction userAction =userInterestOptional.get();
            if(userAction.getPaperCollections()==null){
                return ResponseVO.buildFailure("用户未收藏");
            }
            boolean hasDeleted=false;
            List<PaperCollection> paperCollections= userAction.getPaperCollections();
            for(PaperCollection paperCollection:paperCollections){
                if(paperCollection.getPaperId().equals(paperId)){
                    paperCollections.remove(paperCollection);
                    hasDeleted=true;
                    break;
                }
            }
            if(paperCollections.isEmpty()&& userAction.getClickActions().isEmpty()){
                userActionRepository.deleteById(uid.longValue());
            }
            else{
                userActionRepository.save(userAction);
            }
            return hasDeleted?ResponseVO.buildSuccess("取消收藏成功"):ResponseVO.buildFailure("用户未收藏");
        }
        else {
            return ResponseVO.buildFailure("用户未收藏");
        }
    }

    @Override
    public List<Paper> getUserCollection(Integer uid) {
        Optional<UserAction> userActionOptional=userActionRepository.findById(uid.longValue());
        if(userActionOptional.isPresent()){
            UserAction userAction=userActionOptional.get();
            List<Paper> papers=new ArrayList<>();
            List<PaperCollection> paperCollections=userAction.getPaperCollections();
            for(PaperCollection paperCollection:paperCollections){
                papers.add(paperRepository.findById(paperCollection.getPaperId().longValue()).get());
            }
            return papers;
        }
        else{
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseVO mockUserAction(String prefix,int num) {
        if(getIdByUsername(prefix+"_0")!=-1){
            return ResponseVO.buildFailure("此前缀名已被使用");
        }
        Random random=new Random();
        String[] keys=new String[]{"Bert","nlp","big data","rnn"};
        List<Integer> uid_list=new ArrayList<>();
        for(int i=0;i<num;i++){
            UserPO userPO=new UserPO(prefix+"_"+i,md5("123456"));
            userRepository.save(userPO);
            uid_list.add(userPO.getId());
        }
        for(Integer uid:uid_list){

            List<Paper> papers=paperService.queryPaper(keys[uid%4],0
                    ,20,uid);
            //在20个论文里面随机点击8-12篇论文,每篇随机点击1-4次,每篇随机决定是否收藏
            int paperNumRandom=random.nextInt(5)+8;

            List<Paper> randomPapers=randomGetPaper(papers,paperNumRandom);
            for(Paper paper:randomPapers){
                Long paperId=paper.getId();
                int clickCnt=random.nextInt(4)+1;
                boolean collect=random.nextBoolean();
                for(int i=0;i<clickCnt;i++){
                    clickAction(uid,paperId);
                }
                if(collect){
                    collectPaper(uid,paperId);
                }
            }
            System.out.println("generate user action:"+uid);
        }
        return ResponseVO.buildSuccess("模拟用户成功");
    }

    /**
     * 从base数组中随机选取cnt个不重复的元素
     * @param base
     * @param cnt
     * @return
     */
    private <T> List<T> randomGetPaper(List<T> base,int cnt){
        List<T> res=new ArrayList<>();
        Random random=new Random();
        int size=base.size();
        for(int i=0;i<cnt;i++){
            int index=random.nextInt(size);
            res.add(base.get(index));
            base.remove(index);
            size--;
        }
        return res;
    }

    @Override
    public void recordSearch(Integer uid, String searchContent) {
        Optional<UserSearch> userSearchOptional=userSearchRepository.findById(uid.longValue());
        UserSearch userSearch;
        if(userSearchOptional.isPresent()){
            userSearch=userSearchOptional.get();
            userSearch.getSearchActions().add(new SearchAction(searchContent,new Date().getTime()));
        }
        else{
            userSearch=new UserSearch(uid);
            List<SearchAction> searchActions=new ArrayList<>();
            searchActions.add(new SearchAction(searchContent,new Date().getTime()));
            userSearch.setSearchActions(searchActions);
        }
        userSearchRepository.save(userSearch);
    }


    private static String md5(String password){
        try{
            MessageDigest md=MessageDigest.getInstance("md5");

            byte[] bytes=md.digest(password.getBytes());

            String str= Base64.getEncoder().encodeToString(bytes);

            return str;
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}
