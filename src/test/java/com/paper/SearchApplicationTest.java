package com.paper;

import com.paper.conf.ElasticSearchConfig;
import com.paper.model.vo.Bibl;
import com.paper.model.vo.PaperInfoVO;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchApplicationTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;

//    @Test
//    public void contextLoads() throws IOException {
//        //测试保存
//        IndexRequest request = new IndexRequest("posts");
//        request.id("1");
//        String jsonString = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        request.source(jsonString, XContentType.JSON);
//        IndexResponse index=
//                restHighLevelClient.index(request, ElasticSearchConfig.COMMON_OPTIONS);
//        System.out.println("index-------------:"+ index);
//    }

    @Test
    public void test() throws IOException {
        //搜索address中包含 mill的所有人的年龄分布以及平均年龄
        //  创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("paper_info");
        // 指定 DSL 检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构建检索条件 address 包含 mill
        searchSourceBuilder.query(QueryBuilders.matchQuery("id","Y99-1003").fuzziness(Fuzziness.AUTO));
//        // 按照年龄值分布进行聚合
//        TermsAggregationBuilder ageAgg  = AggregationBuilders.terms("ageAgg").field("age").size(10);
//        searchSourceBuilder.aggregation(ageAgg);
//        // 1.3 计算平均薪资
//        AvgAggregationBuilder  balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
//        searchSourceBuilder.aggregation(balanceAvg);

        searchRequest.source(searchSourceBuilder);

        //执行检索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

        //获取查询到的记录
        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();



        Map resMap=searchHits[0].getSourceAsMap();
        System.out.println(resMap);
        PaperInfoVO paperInfoVO=new PaperInfoVO(resMap);
        System.out.println(paperInfoVO.toString());
//        //获取检索的分析信息
//        Aggregations aggregations = searchResponse.getAggregations();
//        // for (Aggregation aggregation : aggregations.asList()) {
//        //     System.out.println("当前聚合名：" + aggregation.getName());
//        // }
//        Terms ageAgg1 = aggregations.get("ageAgg");
//        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
//            String keyAsString = bucket.getKeyAsString();
//            System.out.println("年龄：" + keyAsString + " 岁的有 " + bucket.getDocCount() + " 人");
//        }
//
//        Avg balanceAvg1 = aggregations.get("balanceAvg");
//        System.out.println("平均薪资: " + balanceAvg1.getValue());

    }
}
