package com.osvaldozunniga.apiSpringBoot.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Document(collection = "cattleStatus")
public class CattleStatus {
    @Id
    private String id;
    private int statusId;
    private String name;
}
/*
IDS 
inactivo=0
activo=1
vendido=2
*/
