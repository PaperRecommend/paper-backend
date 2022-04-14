package com.example.paper.service.impl;

import com.example.paper.entity.vo.StaAuthorVO;
import com.example.paper.entity.vo.StaConferenceVO;
import com.example.paper.entity.vo.StaFieldVO;
import com.example.paper.service.StatisticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {


    @Override
    public List<StaAuthorVO> statisticAuthor() {
        List<StaAuthorVO> list=new ArrayList<>();
        return list;
    }

    @Override
    public List<StaFieldVO> statisticField() {
        return null;
    }

    @Override
    public List<StaConferenceVO> statisticConference() {
        return null;
    }
}
