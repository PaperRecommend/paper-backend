package com.example.paper.dao;

import com.example.paper.entity.vo.StaFieldVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StaFieldRepository extends ElasticsearchRepository<StaFieldVO,Long> {

}
