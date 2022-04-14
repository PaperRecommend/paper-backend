package com.example.paper.entity.userActionEntity;


public class ClickAction {
    private Long paperId;
    private String paperTitle;
    private Integer clickCount;
    private Long lastTime;

    public ClickAction(){}

    public ClickAction(Long paperId, String paperTitle, Integer clickCount,Long lastTime) {
        this.paperId = paperId;
        this.paperTitle = paperTitle;
        this.clickCount = clickCount;
        this.lastTime=lastTime;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
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

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }
}
