package com.example.paper.service;

import com.example.paper.entity.vo.ResponseVO;

public interface UserRecommendService {
    ResponseVO interestSingleUpdate(Integer uid);

    ResponseVO interestAllUpdate();
}