package com.example.paper.dao;

import com.example.paper.entity.ldarecommendEntity.LdaUserPaper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LdaRecommend extends ElasticsearchRepository<LdaUserPaper, Integer> {


}
