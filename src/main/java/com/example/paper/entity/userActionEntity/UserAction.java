package com.example.paper.entity.userActionEntity;

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
@Document(indexName = "user_action")

public class UserAction {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Text)
    private List<ClickAction> clickActions;

    @Field(type = FieldType.Text)
    private List<PaperCollection> paperCollections;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserAction(Integer id) {
        this.id = id;
    }

    public List<ClickAction> getClickActions() {
        return clickActions;
    }

    public void setClickActions(List<ClickAction> clickActions) {
        this.clickActions = clickActions;
    }

    public List<PaperCollection> getPaperCollections() {
        return paperCollections;
    }

    public void setPaperCollections(List<PaperCollection> paperCollections) {
        this.paperCollections = paperCollections;
    }
}
