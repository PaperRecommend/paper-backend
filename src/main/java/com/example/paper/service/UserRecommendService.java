package com.example.paper.service;

import com.example.paper.entity.vo.ResponseVO;

public interface UserRecommendService {
    ResponseVO interestSingleUpdate(Integer uid);

    ResponseVO interestAllUpdate();

    ResponseVO singleUserSimilarity(Integer uid);

    ResponseVO allUserSimilarity();

    ResponseVO recommendSingleUpdate(Integer uid);

    ResponseVO recommendAllUpdate();
}
