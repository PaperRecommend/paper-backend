package com.example.paper.entity.userSimilarityEntity;

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
@Document(indexName = "user_similarity")
public class UserSimilarity {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Text)
    private List<Similarity> similarities;

    public Integer getId() {
        return id;
    }

    public UserSimilarity(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Similarity> getSimilarities() {
        return similarities;
    }

    public void setSimilarities(List<Similarity> similarities) {
        this.similarities = similarities;
    }
}
