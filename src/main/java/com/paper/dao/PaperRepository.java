package com.paper.dao;

import com.paper.entity.Paper;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperRepository extends ElasticsearchRepository<Paper, Long>{
    @Query("{\"multi_match\": {\"query\": \"?0\",\"fields\": [\"title^4\",\"abstract_^2\",\"fos.name\",\"authors.name\"] }}")
    List<Paper> findByKey(String key);
}
