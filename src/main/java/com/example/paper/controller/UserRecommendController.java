package com.example.paper.controller;

import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.UserRecommendService;
import com.example.paper.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="用户推荐接口")
@RestController
@RequestMapping("/api/recommend")
public class UserRecommendController {
    @Autowired
    UserRecommendService userRecommendService;


    @ApiOperation("/重新计算单个用户的兴趣列表")
    @PostMapping("/interest/single-update")
    public ResponseEntity<ResponseVO> interestSingleUpdate(@RequestParam("uid")Integer uid){

        ResponseVO responseVO=userRecommendService.interestSingleUpdate(uid);
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }

    @ApiOperation("/重新计算所有用户的兴趣列表")
    @PostMapping("/interest/all-update")
    public ResponseEntity<ResponseVO> interestAllUpdate(){
        return ResponseUtils.success(userRecommendService.interestAllUpdate());
    }

}
