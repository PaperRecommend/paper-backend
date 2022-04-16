package com.example.paper.entity.userActionEntity;


public class ClickAction {
    private Long paperId;
    private Integer clickCount;
    private Long lastTime;

    public ClickAction(){}

    public ClickAction(Long paperId, Integer clickCount,Long lastTime) {
        this.paperId = paperId;
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

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }
}
