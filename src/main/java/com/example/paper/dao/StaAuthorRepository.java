package com.example.paper.dao;


import com.example.paper.entity.vo.StaAuthorVO;
import com.mongodb.QueryBuilder;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface StaAuthorRepository extends ElasticsearchRepository<StaAuthorVO, Long> {

//    @Query
//    List<StaAuthorVO> search(QueryBuilder queryBuilder);
}
