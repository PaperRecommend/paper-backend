package com.example.paper.service;

import com.example.paper.entity.vo.StaAuthorVO;
import com.example.paper.entity.vo.StaConferenceVO;
import com.example.paper.entity.vo.StaFieldVO;

import java.net.UnknownHostException;
import java.util.List;

public interface StatisticService {

    List<StaAuthorVO> statisticAuthor(int pageNum, int pageSize);

    List<StaFieldVO> statisticField(int pageNum, int pageSize);

    List<StaConferenceVO> statisticConference(int pageNum, int pageSize);

    StaAuthorVO getAuthorDetail(long id);

    StaConferenceVO getConferenceDetail(long id);

    StaFieldVO getFieldDetail(long id);

    Long getFieldIdByName(String name);

}
