package com.example.paper.entity.userRecommendEntity;

public class PaperRecommend {
    private Long paperId;
    private Double weight;

    public PaperRecommend(Long paperId, Double weight) {
        this.paperId = paperId;
        this.weight = weight;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }


}
