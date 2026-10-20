package com.osvaldozunniga.apiSpringBoot.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "cattleStatus")
public class CattleStatus {
    private String id;
    private int statusId;
    private String name;

    public CattleStatus() {
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
