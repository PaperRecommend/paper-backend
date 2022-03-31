package com.example.paper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "dblp")
public class Paper {
    //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
    @Id
    @Field(type = FieldType.Long)
    private Long id;//商品唯一标识



    /**
     * type : 字段数据类型
     * analyzer : 分词器类型
     * index : 是否索引(默认:true)
     * Keyword : 短语,不进行分词
     */

    @Field(type = FieldType.Double,name="_score")
    private Double score;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private Author[] authors;

    @Field(type = FieldType.Integer)
    private Integer year;

    @Field(type = FieldType.Integer)
    private Integer n_citation;

    @Field(type = FieldType.Keyword, index=false)
    private String page_start;

    @Field(type = FieldType.Keyword, index = false)
    private String page_end;

    @Field(type = FieldType.Keyword)
    private String doc_type;

    @Field(type = FieldType.Keyword)
    private String publisher;

    @Field(type = FieldType.Keyword)
    private String volume;

    @Field(type = FieldType.Keyword)
    private String issue;

    @Field(type = FieldType.Keyword, index = false)
    private String doi;

    @Field(type = FieldType.Text)
    private Long[] references;

    @Field(type = FieldType.Text,name = "abstract")
    private String abstract_;

    @Field(type = FieldType.Text)
    private Fos[] fos;

    @Field(type = FieldType.Object, index = false)
    private Venue venue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getN_citation() {
        return n_citation;
    }

    public void setN_citation(Integer n_citation) {
        this.n_citation = n_citation;
    }

    public String getPage_start() {
        return page_start;
    }

    public void setPage_start(String page_start) {
        this.page_start = page_start;
    }

    public String getPage_end() {
        return page_end;
    }

    public void setPage_end(String page_end) {
        this.page_end = page_end;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public Long[] getReferences() {
        return references;
    }

    public void setReferences(Long[] references) {
        this.references = references;
    }

    public String getAbstract_() {
        return abstract_;
    }

    public void setAbstract_(String abstract_) {
        this.abstract_ = abstract_;
    }

    public Fos[] getFos() {
        return fos;
    }

    public void setFos(Fos[] fos) {
        this.fos = fos;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    public Double getScore() {
        return score;
    }

    public void set_score(Double _score) {
        this.score = score;
    }



}

