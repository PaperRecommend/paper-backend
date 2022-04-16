package com.example.paper.entity.userRecommendEntity;

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
@Document(indexName = "user_recommend")
public class UserRecommend {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;


    @Field(type = FieldType.Text)
    private List<PaperRecommend> paperRecommendList;

    public UserRecommend(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PaperRecommend> getPaperRecommendList() {
        return paperRecommendList;
    }

    public void setPaperRecommendList(List<PaperRecommend> paperRecommendList) {
        this.paperRecommendList = paperRecommendList;
    }
}
