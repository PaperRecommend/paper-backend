package com.example.paper.service.impl;

import com.example.paper.config.security.JwtTokenUtils;
import com.example.paper.dao.UserActionRepository;
import com.example.paper.dao.UserRepository;
import com.example.paper.dao.UserSearchRepository;
import com.example.paper.entity.po.UserPO;
import com.example.paper.entity.userActionEntity.ClickAction;
import com.example.paper.entity.userActionEntity.PaperCollection;
import com.example.paper.entity.userActionEntity.UserAction;
import com.example.paper.entity.userSearchEntity.SearchAction;
import com.example.paper.entity.userSearchEntity.UserSearch;
import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.UserService;

import java.util.*;

import com.example.paper.utils.CookieUtils;
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
    public ResponseVO getIdByUsername(String username) {
        UserPO userPO=userRepository.getByName(username);
        return userPO==null?ResponseVO.buildFailure("用户不存在"):ResponseVO.buildSuccess(String.valueOf(userPO.getId()));
    }

    @Override
    public ResponseVO clickAction(Integer uid, Long paperId, String paperTitle) {
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
                clickActions.add(new ClickAction(paperId,paperTitle,1,new Date().getTime()));
            }
        }
        else{
            userAction =new UserAction(uid);
            List<ClickAction> clickActions=new ArrayList<>();
            clickActions.add(new ClickAction(paperId,paperTitle,1,new Date().getTime()));
            List<PaperCollection> paperCollections=new ArrayList<>();
            userAction.setPaperCollections(paperCollections);
            userAction.setClickActions(clickActions);
        }
        userActionRepository.save(userAction);
        return ResponseVO.buildSuccess("点击记录成功");
    }

    @Override
    public ResponseVO collectPaper(Integer uid, Long paperId, String paperTitle) {
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
            paperCollections.add(new PaperCollection(paperId,paperTitle,new Date().getTime()));
        }
        else{
            userAction =new UserAction(uid);
            List<PaperCollection> paperCollections=new ArrayList<>();
            paperCollections.add(new PaperCollection(paperId,paperTitle,new Date().getTime()));
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
