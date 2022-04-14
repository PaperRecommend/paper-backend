package com.example.paper.controller;


import com.example.paper.entity.vo.StaAuthorVO;
import com.example.paper.entity.vo.StaConferenceVO;
import com.example.paper.entity.vo.StaFieldVO;

import com.example.paper.service.StatisticService;
import com.example.paper.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StatisticController {
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    private StatisticService statisticService;

    /**
     *
     * @return
     */
    @GetMapping("/author/list")
    public ResponseEntity<List<StaAuthorVO>> statisticAuthor() {
        return ResponseUtils.success(statisticService.statisticAuthor());
    }


    /**
     *
     * @return
     */
    @GetMapping("/conference/list")
    public ResponseEntity<List<StaConferenceVO>> statisticConference() {
        return ResponseUtils.success(statisticService.statisticConference());
    }

    /**
     *
     * @return
     */
    @GetMapping("/field/list")
    public ResponseEntity<List<StaFieldVO>> statisticField() {
        return ResponseUtils.success(statisticService.statisticField());
    }

}
