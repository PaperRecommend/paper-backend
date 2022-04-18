package com.example.paper.dao;

import com.example.paper.entity.vo.StaFieldVO;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface StaFieldRepository extends ElasticsearchRepository<StaFieldVO,Long> {

    @Query("{\"term\":{\"name.keyword\":\"?0\"}}")
    List<StaFieldVO> getFieldIdByName(String name);

}
