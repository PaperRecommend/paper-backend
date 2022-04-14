package com.example.paper.entity.userSearchEntity;

public class SearchAction {
    private String searchContent;
    private Long lastTime;

    public SearchAction(String searchContent, Long lastTime) {
        this.searchContent = searchContent;
        this.lastTime = lastTime;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }
}
