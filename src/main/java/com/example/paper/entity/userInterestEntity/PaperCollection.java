package com.example.paper.entity.userInterestEntity;

public class PaperCollection {
    private Long paperId;
    private String paperTitle;

    public PaperCollection(Long paperId, String paperTitle) {
        this.paperId = paperId;
        this.paperTitle = paperTitle;
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
}
