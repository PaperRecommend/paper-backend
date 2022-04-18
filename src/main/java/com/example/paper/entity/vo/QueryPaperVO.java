package com.example.paper.entity.vo;

import com.example.paper.entity.paperEntity.Paper;

import java.util.List;

public class QueryPaperVO {
    public List<Paper> papers;
    public int itemCount;

    public QueryPaperVO(List<Paper> papers, int itemCount) {
        this.papers = papers;
        this.itemCount = itemCount;
    }
}
