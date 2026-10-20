package com.osvaldozunniga.apiSpringBoot.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "farmEvents")
public class FarmEvent {
    public FarmEvent() {
    }
    @Id
    private String id;
    private String type;
    private String description;
    private String date;
}
