package com.osvaldozunniga.apiSpringBoot.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "cattle")
public class Cattle {
    public Cattle() {
    }
    @Id
    private String id;
    private String earTag;
    private String name;
    private String breed;
    private String birthday;
    private Double weight;
    private String gender;
    private String image;
    private String motherId;
    private String fatherId;
    private int statusId = 1;
    private String ownerId;
}
