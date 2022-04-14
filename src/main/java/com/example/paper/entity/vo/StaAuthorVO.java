package com.example.paper.entity.vo;



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
@Document(indexName = "dblp_authors")
public class StaAuthorVO {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword, index = false)
    private String name;

    @Field(type = FieldType.Keyword, index = false)
    private String org;

    @Field(type = FieldType.Text)
    private Long[] papers;

    @Field(type = FieldType.Text)
    private List<Object> coauthors;

    @Field(type = FieldType.Integer)
    private Integer paperCount;

    @Field(type = FieldType.Integer)
    private Integer citationCount;

    @Field(type = FieldType.Text)
    private Object[] fields;

    @Field(type = FieldType.Text)
    private Object years;

    @Field(type = FieldType.Integer)
    private Integer h_index;

    @Field(type = FieldType.Integer)
    private Integer heat;

    @Field(type = FieldType.Double)
    private double cited_rate;

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

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public Long[] getPapers() {
        return papers;
    }

    public void setPapers(Long[] papers) {
        this.papers = papers;
    }

    public List<Object> getCoauthors() {
        return coauthors;
    }

    public void setCoauthors(List<Object> coauthors) {
        this.coauthors = coauthors;
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

    public Object[] getFields() {
        return fields;
    }

    public void setFields(Object[] fields) {
        this.fields = fields;
    }

    public Object getYears() {
        return years;
    }

    public void setYears(Object years) {
        this.years = years;
    }

    public Integer getH_index() {
        return h_index;
    }

    public void setH_index(Integer h_index) {
        this.h_index = h_index;
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    public double getCited_rate() {
        return cited_rate;
    }

    public void setCited_rate(double cited_rate) {
        this.cited_rate = cited_rate;
    }
}
