package com.example.paper.dao;

import com.example.paper.entity.userSearchEntity.UserSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<UserSearch,Long> {

}
