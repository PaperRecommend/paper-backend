package com.paper.model.vo;

import java.util.List;
import java.util.Map;

public class Bibl {
    public String id;
    public String title;//文献标题
    public String biblDate; //文献发表时间
    public List<String> authorNameList;
    public String biblTitle; //期刊标题

    public Bibl(Map<String,Object> biblMap){
        this.id= (String) biblMap.get("id");
        this.title=(String) biblMap.get("title");
        this.biblDate=(String) biblMap.get("biblDate");
        this.authorNameList=(List<String>) biblMap.get("authorNameList");
        this.biblTitle=(String) biblMap.get("biblTitle");
    }

    @Override
    public String toString() {
        return "Bibl{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", biblDate='" + biblDate + '\'' +
                ", authorNameList=" + authorNameList +
                ", biblTitle='" + biblTitle + '\'' +
                '}';
    }

}
