package com.example.paper.service;

import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.userRecommendEntity.PaperRecommend;
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

    List<Paper> mixedRecommend(Integer uid, int size);

    Double countAverage(Integer begin_uid,Integer end_uid);
}
