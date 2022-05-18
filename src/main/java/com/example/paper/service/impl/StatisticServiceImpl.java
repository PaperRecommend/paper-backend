package com.example.paper.service.impl;

import com.example.paper.dao.PaperRepository;
import com.example.paper.dao.StaAuthorRepository;
import com.example.paper.dao.StaConferenceRepository;
import com.example.paper.dao.StaFieldRepository;
import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.vo.StaAuthorVO;
import com.example.paper.entity.vo.StaConferenceVO;
import com.example.paper.entity.vo.StaFieldVO;
import com.example.paper.service.StatisticService;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private StaAuthorRepository staAuthorRepository;

    @Autowired
    private StaFieldRepository staFieldRepository;

    @Autowired
    private StaConferenceRepository staConferenceRepository;

    @Override
    public List<StaAuthorVO> statisticAuthor(int pageNum, int pageSize) {


        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        FieldSortBuilder fsb = SortBuilders.fieldSort("heat").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
//        Iterable<StaAuthorVO> search = staAuthorRepository.findAll(Sort.by(Sort.Direction.DESC,"heat"));

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).withSort(fsb).withPageable(pageable).build();
        Page<StaAuthorVO> page = staAuthorRepository.search(query);

        List<StaAuthorVO> result = new ArrayList<>();
        for (StaAuthorVO staAuthorVO : page) {
            result.add(staAuthorVO);
        }
        return result;
    }

    @Override
    public List<StaFieldVO> statisticField(int pageNum, int pageSize) {
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        FieldSortBuilder fsb = SortBuilders.fieldSort("heat").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
//        Iterable<StaAuthorVO> search = staAuthorRepository.findAll(Sort.by(Sort.Direction.DESC,"heat"));

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).withSort(fsb).withPageable(pageable).build();
        Page<StaFieldVO> page = staFieldRepository.search(query);

        List<StaFieldVO> result = new ArrayList<>();
        for (StaFieldVO staFieldVO : page) {
            result.add(staFieldVO);
        }
        return result;
    }

    @Override
    public List<StaConferenceVO> statisticConference(int pageNum, int pageSize) {

        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        FieldSortBuilder fsb = SortBuilders.fieldSort("heat").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
//        Iterable<StaAuthorVO> search = staAuthorRepository.findAll(Sort.by(Sort.Direction.DESC,"heat"));

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).withSort(fsb).withPageable(pageable).build();
        Page<StaConferenceVO> page = staConferenceRepository.search(query);

        List<StaConferenceVO> result = new ArrayList<>();
        for (StaConferenceVO staConferenceVO : page) {
            result.add(staConferenceVO);
        }
        return result;
    }

    @Override
    public StaAuthorVO getAuthorDetail(long id) {

        Optional<StaAuthorVO> opt = staAuthorRepository.findById(id);
        StaAuthorVO staAuthorVO = null;
        if (opt.isPresent()) {
            staAuthorVO = opt.get();
            List<Paper> papers=new ArrayList<>();
            for(Long paperId:staAuthorVO.getPapers()){
                Optional<Paper> paper=paperRepository.findById(paperId);
                paper.ifPresent(papers::add);
            }
            staAuthorVO.setPaperDetail(papers);
        }
        return staAuthorVO;
    }

    @Override
    public StaConferenceVO getConferenceDetail(long id) {
        Optional<StaConferenceVO> opt = staConferenceRepository.findById(id);
        StaConferenceVO staConferenceVO = null;
        if (opt.isPresent()) {
            staConferenceVO = opt.get();

        }
        return staConferenceVO;
    }

    @Override
    public StaFieldVO getFieldDetail(long id) {
        Optional<StaFieldVO> opt = staFieldRepository.findById(id);
        StaFieldVO staFieldVO = null;
        if (opt.isPresent()) {
            staFieldVO = opt.get();
            List<Paper> papers=new ArrayList<>();
            for(Long paperId:staFieldVO.getPapers()){
                Optional<Paper> paper=paperRepository.findById(paperId);
                paper.ifPresent(papers::add);
                if(papers.size()>60){
                    break;
                }
            }
            papers.sort(new Comparator<Paper>() {
                @Override
                public int compare(Paper o1, Paper o2) {
                    return o2.getN_citation()-o1.getN_citation();
                }
            });
            staFieldVO.setPaperDetail(papers);
            List<StaAuthorVO> authorDetail=new ArrayList<>();
            for(Long authorId: staFieldVO.getAuthors()){
                Optional<StaAuthorVO> author_opt = staAuthorRepository.findById(authorId);
                author_opt.ifPresent(authorDetail::add);

            }
            authorDetail.sort(new Comparator<StaAuthorVO>() {
                @Override
                public int compare(StaAuthorVO o1, StaAuthorVO o2) {
                    return o2.getHeat()-o1.getHeat();
                }
            });
            staFieldVO.setAuthorDetail(authorDetail);
        }
        return staFieldVO;
    }

    @Override
    public Long getFieldIdByName(String name) {
        List<StaFieldVO> list=staFieldRepository.getFieldIdByName(name);
        Long id=-1L;
        if(list.size()!=0){
            id=list.get(0).getId();
        }
        return id;
    }
}
