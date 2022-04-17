package com.example.paper.controller;

import com.example.paper.entity.paperEntity.Paper;
import com.example.paper.entity.vo.PaperSummaryVO;
import com.example.paper.service.PaperService;
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
@RequestMapping("/api/query")
public class PaperController {
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);
    @Autowired
    private PaperService paperService;
    /**
     * 初次数据查询
     *
     * @param query        : 查询关键字
     * @param returnFacets : 返回具体类型
     * @param pageNum      : 页号
     * @param pageSize     : 页大小
     * @return json
     */
    @GetMapping("/paper/list")
    public ResponseEntity<List<Paper>> queryPaper(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "returnFacets") String returnFacets,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request,
            HttpServletResponse response){
        return ResponseUtils.success(paperService.queryPaper(query,returnFacets,pageNum,pageSize,request, response));
    }

    /**
     * 进一步筛选查询
     *
     * @param refinements : 新的限制. year , title , conferences , terms , keywords , authors , affiliations
     * @param pageNum     : 页号
     * @param pageSize    : 页大小
     */
    @GetMapping("/paper/refine")
    public ResponseEntity<List<Paper>> queryPaperRefine(
            @CookieValue(name = "qid") String qid,
            @RequestParam(name = "refinements") List<String> refinements,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        return ResponseUtils.success(paperService.queryPaperRefine(qid, refinements, pageNum, pageSize, request));
    }

    /**
     * 获取指定的查询id下的论文summary
     *
     * @param qid : query id
     */
    @GetMapping(path = "/paper/summary")
    public ResponseEntity<PaperSummaryVO> getPaperSummary(@CookieValue(name = "qid") String qid,
                                                          HttpServletRequest request) {
        System.out.println(qid);
        return ResponseUtils.success(paperService.papersSummary(qid, request));
    }

}

