package com.example.paper.entity.userSearchEntity;


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
@Document(indexName = "user_search")
public class UserSearch {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Text)
    private List<SearchAction> searchActions;

    public UserSearch(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SearchAction> getSearchActions() {
        return searchActions;
    }

    public void setSearchActions(List<SearchAction> searchActions) {
        this.searchActions = searchActions;
    }
}
