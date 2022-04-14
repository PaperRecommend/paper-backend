package com.example.paper.entity.userActionEntity;

public class PaperCollection {
    private Long paperId;
    private String paperTitle;
    private Long lastTime;


    public PaperCollection(Long paperId, String paperTitle,Long lastTime) {
        this.paperId = paperId;
        this.paperTitle = paperTitle;
        this.lastTime=lastTime;
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

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }
}
