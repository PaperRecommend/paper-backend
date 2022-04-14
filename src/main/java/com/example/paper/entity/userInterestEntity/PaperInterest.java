package com.example.paper.entity.userInterestEntity;

public class PaperInterest {
    private Long paperId;
    private String paperTitle;
    private Double interest;

    public PaperInterest(Long paperId, String paperTitle, Double interest) {
        this.paperId = paperId;
        this.paperTitle = paperTitle;
        this.interest = interest;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }
}
