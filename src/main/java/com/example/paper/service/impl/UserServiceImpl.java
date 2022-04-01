package com.example.paper.service.impl;

import com.example.paper.config.security.JwtTokenUtils;
import com.example.paper.dao.UserRepository;
import com.example.paper.entity.po.UserPO;
import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.UserService;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

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
