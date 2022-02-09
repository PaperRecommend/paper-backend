package com.paper.model.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaperInfoVO {
    public String id;

    public List<String> keywordList;

    public String abstractText;

    public String title;

    public List<Bibl> biblList;

    public List<String> authorsList;

    public PaperInfoVO(){}

    public PaperInfoVO(Map<String,Object> resMap){
        this.id=(String) resMap.get("id");
        this.keywordList= (List<String>) resMap.get("keywordList");
        this.abstractText=(String) resMap.get("abstract");
        this.title=(String) resMap.get("title");
        biblList=new ArrayList<Bibl>();
        List<Map<String,Object>> list=(List<Map<String,Object>>) resMap.get("biblList");
        for(Map<String,Object> bibl:list){
            biblList.add(new Bibl(bibl));
        }
        this.authorsList=(List<String>) resMap.get("authorsList");
    }

    @Override
    public String toString() {
        return "PaperInfoVO{" +
                "id='" + id + '\'' +
                ", keywordList=" + keywordList +
                ", abstractText='" + abstractText + '\'' +
                ", title='" + title + '\'' +
                ", biblList=" + biblList +
                ", authorsList=" + authorsList +
                '}';
    }
}
