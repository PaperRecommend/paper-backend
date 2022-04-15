package com.example.paper.entity.userSimilarityEntity;

public class Similarity {
    private Integer uid;
    private double similarity;

    public Similarity(Integer uid, double similarity) {
        this.uid = uid;
        this.similarity = similarity;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
