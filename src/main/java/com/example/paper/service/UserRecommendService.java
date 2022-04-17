package com.example.paper.service;

import com.example.paper.entity.userRecommendEntity.PaperRecommend;
import com.example.paper.entity.userRecommendEntity.UserRecommend;
import com.example.paper.entity.vo.ResponseVO;

import java.util.List;

public interface UserRecommendService {
    ResponseVO interestSingleUpdate(Integer uid);

    ResponseVO interestAllUpdate();

    ResponseVO singleUserSimilarity(Integer uid);

    ResponseVO allUserSimilarity();

    ResponseVO recommendSingleUpdate(Integer uid);

    ResponseVO recommendAllUpdate();

    List<PaperRecommend> getUserRecommend(Integer uid, int n);
}
