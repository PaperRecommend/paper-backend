package com.paper.controller;


import com.paper.model.vo.PaperInfoVO;
import com.paper.model.vo.ResponseVO;
import com.paper.service.PaperInfoService;
import com.paper.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags="论文信息接口")
@RestController
@RequestMapping("/paperInfo")
public class PaperInfoController {
    @Autowired
    PaperInfoService paperInfoService;

    @ApiOperation("通过id查询论文信息")
    @GetMapping("/getById")
    @ApiImplicitParam(paramType="query", name = "id", value = "论文id", required = true, dataType = "String")
    ResponseEntity<PaperInfoVO> getPaperInfoById(@RequestParam String id){
        PaperInfoVO info = null;
        try {
            info=paperInfoService.getPaperInfoById(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(info==null){
            return ResponseUtils.badRequest(info);
        }
        return ResponseUtils.success(info);
    }


}
