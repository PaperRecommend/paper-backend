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
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StatisticController {
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    private StatisticService statisticService;

    /**
     * @return
     */
    @GetMapping("/author/list")
    public ResponseEntity<List<StaAuthorVO>> statisticAuthor(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseUtils.success(statisticService.statisticAuthor(pageNum,pageSize));
    }
    
    /**
     * @return
     */
    @GetMapping("/conference/list")
    public ResponseEntity<List<StaConferenceVO>> statisticConference(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseUtils.success(statisticService.statisticConference(pageNum,pageSize));
    }

    /**
     * @return
     */
    @GetMapping("/field/list")
    public ResponseEntity<List<StaFieldVO>> statisticField(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseUtils.success(statisticService.statisticField(pageNum,pageSize));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/author/detail")
    public ResponseEntity<StaAuthorVO> getAuthorDetail(@RequestParam(name = "id") long id) {
        return ResponseUtils.success(statisticService.getAuthorDetail(id));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/conference/detail")
    public ResponseEntity<StaConferenceVO> getConferenceDetail(@RequestParam(name = "id") long id) {
        return ResponseUtils.success(statisticService.getConferenceDetail(id));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/field/detail")
    public ResponseEntity<StaFieldVO> getFieldDetail(@RequestParam(name = "id") long id) {
        return ResponseUtils.success(statisticService.getFieldDetail(id));
    }

}
