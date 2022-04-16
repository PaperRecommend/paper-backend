package com.example.paper.dao;

import com.example.paper.entity.userRecommendEntity.UserRecommend;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRecommendRepository extends ElasticsearchRepository<UserRecommend,Long> {
}
