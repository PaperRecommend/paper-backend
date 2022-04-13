package com.example.paper.entity.userInterestEntity;

public class ClickAction {
    private Long paperId;
    private String paperTitle;
    private Integer clickCount;

    public ClickAction(){}

    public ClickAction(Long paperId, String paperTitle, Integer clickCount) {
        this.paperId = paperId;
        this.paperTitle = paperTitle;
        this.clickCount = clickCount;
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
