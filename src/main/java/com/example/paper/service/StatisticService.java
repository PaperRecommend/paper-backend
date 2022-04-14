package com.example.paper.service;

import com.example.paper.entity.vo.StaAuthorVO;
import com.example.paper.entity.vo.StaConferenceVO;
import com.example.paper.entity.vo.StaFieldVO;

import java.util.List;

public interface StatisticService {

    List<StaAuthorVO> statisticAuthor();

    List<StaFieldVO> statisticField();

    List<StaConferenceVO> statisticConference();

}
