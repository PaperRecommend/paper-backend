package com.example.paper.service.impl;


import com.alibaba.fastjson.JSONObject;

import com.example.paper.dao.PaperRepository;
import com.example.paper.entity.Paper;
import com.example.paper.service.PaperService;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Service
@Transactional
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperRepository paperRepository;

    @Override
    @Cacheable(cacheNames = "queryPaper", unless = "#result==null")
    public JSONObject queryPaper(final String key, final String returnFacets,
                                 int pageSize, int pageNum,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {


        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(key, "title", "name");

        List<Paper> paperList=paperRepository.findByKey(key);
        for(Paper paper:paperList){
            System.out.println(paper.getScore());
            System.out.println(paper.getTitle());

            System.out.println(paper.getAuthors()[0].getName());
            System.out.println(paper.getYear());
            System.out.println(paper.getN_citation());
            System.out.println(paper.getDoi());
            System.out.println(paper.getFos()[0].getName());
            System.out.println(paper.getPage_start());
            System.out.println(paper.getPage_end());
            System.out.println(paper.getDoc_type());
            System.out.println(paper.getReferences()[0]);
            System.out.println(paper.getFos()[0].getName());
            System.out.println(paper.getVenue().getRaw());

            System.out.println(paper.getAbstract_());
            break;
        }

        JSONObject result=new JSONObject();
        result.put("papers",paperList);

        return result;

    }


}
