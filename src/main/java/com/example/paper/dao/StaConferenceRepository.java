package com.example.paper.dao;

import com.example.paper.entity.vo.StaConferenceVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StaConferenceRepository extends ElasticsearchRepository<StaConferenceVO,Long> {
}
