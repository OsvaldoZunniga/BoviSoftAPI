package com.osvaldozunniga.apiSpringBoot.model.dto;

import lombok.Data;

@Data
public class FarmEventRequestDto {
    private String type;
    private String description;
    private String date;

    public FarmEventRequestDto() {
    }
}
