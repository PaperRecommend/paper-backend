package com.example.paper.service;

import com.example.paper.entity.po.UserPO;
import com.example.paper.entity.vo.ResponseVO;

public interface UserService {
    int getUserCount();

    /**
     * 注册用户
     * @return 是否成功
     */
    ResponseVO registerUser(String name, String password);

    /**
     * 登录
     * @return 如果成功，msg为token； 如果失败，msg为错误信息
     */
    ResponseVO login(String name, String password);


    UserPO getUserById(int id);



}
