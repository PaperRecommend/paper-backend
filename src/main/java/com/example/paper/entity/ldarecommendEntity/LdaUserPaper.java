package com.example.paper.entity.ldarecommendEntity;

import io.swagger.models.auth.In;
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
@Document(indexName = "user_lda_recommend")
public class LdaUserPaper {

    @Id
    @Field(type = FieldType.Integer)
    private Integer id;


    @Field(type = FieldType.Text)
    private Long[] papers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long[] getPapers() {
        return papers;
    }

    public void setPapers(Long[] papers) {
        this.papers = papers;
    }
}