package com.example.paper.controller;

import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.userRecommendEntity.PaperRecommend;
import com.example.paper.entity.vo.ResponseVO;
import com.example.paper.service.UserRecommendService;
import com.example.paper.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="用户推荐接口")
@RestController
@RequestMapping("/api/recommend")
public class UserRecommendController {
    @Autowired
    UserRecommendService userRecommendService;


    @ApiOperation("重新计算单个用户的兴趣列表")
    @PostMapping("/interest/single-update")
    public ResponseEntity<ResponseVO> interestSingleUpdate(@RequestParam("uid")Integer uid){

        ResponseVO responseVO=userRecommendService.interestSingleUpdate(uid);
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }

    @ApiOperation("重新计算所有用户的兴趣列表")
    @PostMapping("/interest/all-update")
    public ResponseEntity<ResponseVO> interestAllUpdate(){
        return ResponseUtils.success(userRecommendService.interestAllUpdate());
    }

    @ApiOperation("重新计算单个用户与其他用户的相似度")
    @PostMapping("/similarity/single-update")
    public ResponseEntity<ResponseVO> similaritySingleUpdate(@RequestParam("uid")Integer uid){
        return ResponseUtils.success(userRecommendService.singleUserSimilarity(uid));
    }

    @ApiOperation("重新计算所有用户与其他用户相似度")
    @PostMapping("/similarity/all-update")
    public ResponseEntity<ResponseVO> similarityAllUpdate(){
        return ResponseUtils.success(userRecommendService.allUserSimilarity());
    }

    @ApiOperation("计算用户的推荐论文")
    @PostMapping("/paper-recommend/single-update")
    public ResponseEntity<ResponseVO> recommendSingleUpdate(Integer uid){

        userRecommendService.interestSingleUpdate(uid);
        userRecommendService.singleUserSimilarity(uid);
        ResponseVO responseVO=userRecommendService.recommendSingleUpdate(uid);
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }

    @ApiOperation("/计算所有用户的推荐论文")
    @PostMapping("/paper-recommend/all-update")
    public ResponseEntity<ResponseVO> recommendAllUpdate(){
        userRecommendService.interestAllUpdate();
        userRecommendService.allUserSimilarity();
        ResponseVO responseVO=userRecommendService.recommendAllUpdate();
        return responseVO.isSuccess()?ResponseUtils.success(responseVO):ResponseUtils.failure(responseVO);
    }

    @ApiOperation("/获取用户的推荐论文(Top n)")
    @GetMapping("/paper-recommend/user_top")
    public ResponseEntity<List<PaperRecommend>> getUserRecommend(@RequestParam("uid")Integer uid,
                                                                 @RequestParam(name = "n",defaultValue = "10")int n){
        List<PaperRecommend> paperRecommends=userRecommendService.getUserRecommend(uid,n);
        return paperRecommends.isEmpty()?ResponseUtils.failure(paperRecommends):ResponseUtils.success(paperRecommends);
    }

    @ApiOperation("/计算用户的lda推荐论文")
    @GetMapping("/lda-topic/cal_papers")
    public ResponseEntity<String> calUserRecommend(@RequestParam("uid")Integer uid){
        RestTemplate restTemplate=new RestTemplate();
        Map<String, Integer> params=new HashMap<>();
        params.put("uid",uid);  //
        ResponseEntity<String> response=restTemplate.getForEntity("http://172.29.7.234:5000/api/get-single-recommend/?uid={uid}",String.class,params);
        return response;
    }

    @ApiOperation("/获取用户的混合推荐论文")
    @GetMapping("/paper-recommend/mixed")
    public ResponseEntity<List<Paper>> mixedRecommend(@RequestParam("uid")Integer uid,
                                                      @RequestParam("size")int size){
        List<Paper> recommendList=userRecommendService.mixedRecommend(uid,size);
        return ResponseUtils.success(recommendList);
    }

    @ApiOperation("/计算uid区间内的用户平均协同过滤推荐论文数量")
    @GetMapping("/paper-recommend/count_average")
    public ResponseEntity<Double> countAverage(@RequestParam("begin_uid")Integer begin_uid,
                                               @RequestParam("end_uid")Integer end_uid){
        Double average= userRecommendService.countAverage(begin_uid,end_uid);
        return ResponseUtils.success(average);
    }

}
