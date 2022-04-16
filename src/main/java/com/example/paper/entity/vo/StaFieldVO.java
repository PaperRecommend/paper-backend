package com.example.paper.entity.vo;

import com.example.paper.entity.paperEntity.Paper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "dblp_fields")
public class StaFieldVO {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword, index = false)
    private String name;


    @Field(type = FieldType.Text)
    private Long[] papers;

    @Field(type = FieldType.Text)
    private List<Paper> paperDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long[] getPapers() {
        return papers;
    }

    public void setPapers(Long[] papers) {
        this.papers = papers;
    }

    public List<Paper> getPaperDetail() {
        return paperDetail;
    }

    public void setPaperDetail(List<Paper> paperDetail) {
        this.paperDetail = paperDetail;
    }

    public Integer getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(Integer paperCount) {
        this.paperCount = paperCount;
    }

    public Integer getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(Integer citationCount) {
        this.citationCount = citationCount;
    }

    public Object getYears() {
        return years;
    }

    public void setYears(Object years) {
        this.years = years;
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    @Field(type = FieldType.Integer)
    private Integer paperCount;

    @Field(type = FieldType.Integer)
    private Integer citationCount;


    @Field(type = FieldType.Text)
    private Object years;

    @Field(type = FieldType.Integer)
    private Integer heat;

}
