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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        if(res.isSuccess()){
            return ResponseUtils.success(res);
        }
        else{
            return ResponseUtils.failure(res);
        }
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseEntity<ResponseVO> login(@RequestBody UserLoginVO user, HttpServletRequest request,
                                            HttpServletResponse response){
        ResponseVO res=userService.login(user.getName(),user.getPassword(),request,response);
        if(res.isSuccess()){
            return ResponseUtils.success(res);
        }
        else{
            return ResponseUtils.failure(res);
        }
    }

    @ApiOperation("获取用户id")
    @GetMapping("/uid")
    public ResponseEntity<ResponseVO> uid(@RequestParam(name="username")String username){
        ResponseVO responseVO=userService.getIdByUsername(username);
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }



    @ApiOperation("点击事件保存的论文id和论文title")
    @PostMapping("/click")
    public ResponseEntity<ResponseVO> clickAction(@RequestParam(name="uid")Integer uid,
                                                  @RequestParam(name="paperId")Long paperId,
                                                  @RequestParam(name="paperTitle")String paperTitle){

        return ResponseUtils.success(userService.clickAction(uid,paperId,paperTitle));
    }

    @ApiOperation("收藏事件")
    @PostMapping("/collection")
    public ResponseEntity<ResponseVO> collection(@RequestParam(name="uid")Integer uid,
                                                 @RequestParam(name="paperId")Long paperId,
                                                 @RequestParam(name="paperTitle")String paperTitle){
        ResponseVO responseVO=userService.collectPaper(uid,paperId,paperTitle);
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }

    @ApiOperation("取消收藏事件")
    @PostMapping("/cancel-collection")
    public ResponseEntity<ResponseVO> cancelCollection(@RequestParam(name="uid")Integer uid,
                                                       @RequestParam(name="paperId")Long paperId){
        ResponseVO responseVO=userService.cancelCollection(uid,paperId);
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }

}
