package com.example.paper.entity.userInterestEntity;

public class PaperInterest {
    private Long paperId;
    private Double interest;

    public PaperInterest(Long paperId, Double interest) {
        this.paperId = paperId;
        this.interest = interest;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }
}
