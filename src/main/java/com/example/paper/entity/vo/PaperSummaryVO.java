package com.example.paper.entity.vo;

import com.example.paper.utils.Pair;

import java.util.List;

public class PaperSummaryVO {
    public List<Pair<String,Integer>> term;
    public List<Pair<String,Integer>> author;
    public List<Pair<String,Integer>> conference;
    public List<Pair<String,Integer>> publisher;


    public PaperSummaryVO(List<Pair<String,Integer>> term, List<Pair<String,Integer>> author,
                          List<Pair<String,Integer>> conference, List<Pair<String,Integer>> publisher) {
        this.term = term;
        this.author = author;
        this.conference = conference;
        this.publisher = publisher;
    }
}
