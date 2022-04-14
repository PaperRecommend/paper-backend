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
@Document(indexName = "dblp_conferences")
public class StaConferenceVO {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword, index = false)
    private String raw;

    @Field(type = FieldType.Keyword, index = false)
    private String type;

    @Field(type = FieldType.Text)
    private Long[] papers;


    @Field(type = FieldType.Integer)
    private Integer paperCount;

    @Field(type = FieldType.Integer)
    private Integer citationCount;


    @Field(type = FieldType.Text)
    private Object years;


    @Field(type = FieldType.Integer)
    private Integer heat;

}
