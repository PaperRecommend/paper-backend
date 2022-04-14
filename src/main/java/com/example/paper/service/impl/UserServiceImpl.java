package com.example.paper.service.impl;

import com.example.paper.config.security.JwtTokenUtils;
import com.example.paper.dao.UserInterestRepository;
import com.example.paper.dao.UserRepository;
import com.example.paper.entity.po.UserPO;
import com.example.paper.entity.userInterestEntity.ClickAction;
import com.example.paper.entity.userInterestEntity.PaperCollection;
import com.example.paper.entity.userInterestEntity.UserInterest;
import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.UserService;

import java.util.ArrayList;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInterestRepository userInterestRepository;

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
    public ResponseVO login(String name, String password) {
        UserPO userPO=userRepository.getByName(name);

        if(userPO==null){
            return ResponseVO.buildFailure("用户名不存在");
        }
        if(userPO.getPassword().equals(md5(password))){
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
        Optional<UserInterest> userInterestOptional=userInterestRepository.findById(uid.longValue());
        UserInterest userInterest;
        if(userInterestOptional.isPresent()){
            userInterest=userInterestOptional.get();
            List<ClickAction> clickActions=userInterest.getClickActions();

            boolean hasClickedBefore=false;
            for(ClickAction clickAction:clickActions){
                if(clickAction.getPaperId().equals(paperId)){
                    clickAction.setClickCount(clickAction.getClickCount()+1);
                    hasClickedBefore=true;
                    break;
                }
            }
            if(!hasClickedBefore){
                clickActions.add(new ClickAction(paperId,paperTitle,1));
            }
        }
        else{
            userInterest=new UserInterest(uid);
            List<ClickAction> clickActions=new ArrayList<>();
            clickActions.add(new ClickAction(paperId,paperTitle,1));
            List<PaperCollection> paperCollections=new ArrayList<>();
            userInterest.setPaperCollections(paperCollections);
            userInterest.setClickActions(clickActions);
        }
        userInterestRepository.save(userInterest);
        return ResponseVO.buildSuccess("点击记录成功");
    }

    @Override
    public ResponseVO collectPaper(Integer uid, Long paperId, String paperTitle) {
        Optional<UserInterest> userInterestOptional=userInterestRepository.findById(uid.longValue());
        UserInterest userInterest;
        if(userInterestOptional.isPresent()){
            userInterest=userInterestOptional.get();
            List<PaperCollection> paperCollections=userInterest.getPaperCollections();

            for(PaperCollection paperCollection:paperCollections){
                if(paperCollection.getPaperId().equals(paperId)) {
                    return ResponseVO.buildFailure("已经收藏过了");
                }
            }
            paperCollections.add(new PaperCollection(paperId,paperTitle));
        }
        else{
            userInterest=new UserInterest(uid);
            List<PaperCollection> paperCollections=new ArrayList<>();
            paperCollections.add(new PaperCollection(paperId,paperTitle));
            List<ClickAction> clickActions=new ArrayList<>();
            userInterest.setClickActions(clickActions);
            userInterest.setPaperCollections(paperCollections);
        }
        userInterestRepository.save(userInterest);
        return ResponseVO.buildSuccess("收藏成功");
    }

    @Override
    public ResponseVO cancelCollection(Integer uid, Long paperId) {
        Optional<UserInterest> userInterestOptional=userInterestRepository.findById(uid.longValue());
        if(userInterestOptional.isPresent()){
            UserInterest userInterest=userInterestOptional.get();
            if(userInterest.getPaperCollections()==null){
                return ResponseVO.buildFailure("用户未收藏");
            }
            boolean hasDeleted=false;
            List<PaperCollection> paperCollections=userInterest.getPaperCollections();
            for(PaperCollection paperCollection:paperCollections){
                if(paperCollection.getPaperId().equals(paperId)){
                    paperCollections.remove(paperCollection);
                    hasDeleted=true;
                    break;
                }
            }
            if(paperCollections.isEmpty()&&userInterest.getClickActions().isEmpty()){
                userInterestRepository.deleteById(uid.longValue());
            }
            else{
                userInterestRepository.save(userInterest);
            }
            return hasDeleted?ResponseVO.buildSuccess("取消收藏成功"):ResponseVO.buildFailure("用户未收藏");
        }
        else {
            return ResponseVO.buildFailure("用户未收藏");
        }
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
