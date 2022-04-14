package com.example.paper.dao;

import com.example.paper.entity.userInterestEntity.UserInterest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInterestRepository extends ElasticsearchRepository<UserInterest,Long> {
}
