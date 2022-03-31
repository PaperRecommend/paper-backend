package com.example.paper.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PaperService {

    JSONObject queryPaper(final String key, final String returnFacets,
                          int pageSize, int pageNum,
                          HttpServletRequest request,
                          HttpServletResponse response);


}