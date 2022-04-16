package com.example.paper.dao;

import com.example.paper.entity.userSimilarityEntity.UserSimilarity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSimilarityRepository extends ElasticsearchRepository<UserSimilarity,Long> {
}
