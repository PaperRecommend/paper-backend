package com.paper.service;

import com.paper.model.vo.PaperInfoVO;
import org.springframework.stereotype.Service;
import java.io.IOException;

public interface PaperInfoService {
    /**
     * 获取论文基本信息
     * @param id
     * @return
     */
    PaperInfoVO getPaperInfoById(String id) throws IOException;
}
