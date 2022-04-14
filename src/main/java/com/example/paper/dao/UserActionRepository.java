package com.example.paper.dao;

import com.example.paper.entity.userActionEntity.UserAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserActionRepository extends ElasticsearchRepository<UserAction,Long> {

}
