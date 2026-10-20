package com.osvaldozunniga.apiSpringBoot.model.dto;

import com.osvaldozunniga.apiSpringBoot.model.entity.FarmEvent;

import lombok.Data;

@Data
public class FarmEventResponseDto {
    private String id;
    private String type;
    private String description;
    private String date;

    public FarmEventResponseDto() {
    }

    public static FarmEventResponseDto fromEntity(FarmEvent farmEvent) {
        if (farmEvent == null) {
            return null;
        }
        FarmEventResponseDto responseDto = new FarmEventResponseDto();
        responseDto.setId(farmEvent.getId());
        responseDto.setType(farmEvent.getType());
        responseDto.setDescription(farmEvent.getDescription());
        responseDto.setDate(farmEvent.getDate());
        return responseDto;
    }
}
