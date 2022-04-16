package com.example.paper.service;

import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.po.UserPO;
import com.example.paper.entity.userActionEntity.PaperCollection;
import com.example.paper.entity.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    ResponseVO login(String name, String password, HttpServletRequest request,
                     HttpServletResponse response);


    UserPO getUserById(int id);

    Integer getIdByUsername(String username);

    ResponseVO clickAction(Integer uid,Long paperId);

    ResponseVO collectPaper(Integer uid,Long paperId);

    ResponseVO cancelCollection(Integer uid,Long paperId);

    List<Paper> getUserCollection(Integer uid);

    void recordSearch(Integer uid,String searchContent);

}
