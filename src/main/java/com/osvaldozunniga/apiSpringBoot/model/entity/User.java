package com.osvaldozunniga.apiSpringBoot.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "users")
public class User {
    public User() {
    }
    @Id
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
}
