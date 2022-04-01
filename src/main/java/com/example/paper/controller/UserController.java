package com.example.paper.controller;

import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.entity.vo.UserLoginVO;
import com.example.paper.service.UserService;
import com.example.paper.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags="用户模块接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("获取用户数量")
    @GetMapping("/count")
    public ResponseEntity<Integer> getUserCount(){
        return ResponseUtils.success(userService.getUserCount());
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseEntity<ResponseVO> register(@RequestBody UserLoginVO user){
        ResponseVO res=userService.registerUser(user.getName(),user.getPassword());
        System.out.println("zhuce");
        if(res.isSuccess()){
            return ResponseUtils.success(res);
        }
        else{
            return ResponseUtils.failure(res);
        }
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseEntity<ResponseVO> login(@RequestBody UserLoginVO user){
        ResponseVO res=userService.login(user.getName(),user.getPassword());
        if(res.isSuccess()){
            return ResponseUtils.success(res);
        }
        else{
            return ResponseUtils.failure(res);
        }
    }

}
