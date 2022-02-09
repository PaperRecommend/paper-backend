package com.paper.service.impl;

import com.paper.conf.ElasticSearchConfig;
import com.paper.model.vo.PaperInfoVO;
import com.paper.service.PaperInfoService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaperInfoServiceImpl implements PaperInfoService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public PaperInfoVO getPaperInfoById(String id) throws IOException {
        //创建检索请求
        SearchRequest searchRequest = new SearchRequest();

        // 指定索引
        searchRequest.indices("paper_info");

        // 指定 DSL 检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        System.out.println(id);
        //检索id
        searchSourceBuilder.query(QueryBuilders.matchQuery("id",id));
        searchRequest.source(searchSourceBuilder);

        //执行检索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

        //获取查询到的记录
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        if(searchHits.length==0){
            return null;
        }
        return new PaperInfoVO(searchHits[0].getSourceAsMap());
    }
}
