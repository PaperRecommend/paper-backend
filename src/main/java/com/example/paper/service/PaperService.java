package com.example.paper.service;

import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.vo.PaperSummaryVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PaperService {

    List<Paper> queryPaper(final String key, final String returnFacets,
                           int pageNum, int pageSize, HttpServletRequest request,
                           HttpServletResponse response);

    List<Paper> queryPaper(String key,int pageNum, int pageSize,Integer uid);

    PaperSummaryVO papersSummary(String qid,HttpServletRequest request);

    List<Paper> queryPaperRefine(String qid,List<String> refinements,
                                 int pageNum, int pageSize,HttpServletRequest request);

}
