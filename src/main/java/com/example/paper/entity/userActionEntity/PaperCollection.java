package com.example.paper.entity.userActionEntity;

public class PaperCollection {
    private Long paperId;
    private Long lastTime;


    public PaperCollection(Long paperId,Long lastTime) {
        this.paperId = paperId;
        this.lastTime=lastTime;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

}
