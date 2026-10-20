package com.osvaldozunniga.apiSpringBoot.model.dto;

import com.osvaldozunniga.apiSpringBoot.model.entity.Cattle;

import lombok.Data;

@Data
public class CattleResponseDto {
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
    private int statusId;
    private String ownerId;

    public CattleResponseDto() {
    }

    public static CattleResponseDto fromEntity(Cattle cattle) {
        if (cattle == null) {
            return null;
        }
        CattleResponseDto responseDto = new CattleResponseDto();
        responseDto.setId(cattle.getId());
        responseDto.setEarTag(cattle.getEarTag());
        responseDto.setName(cattle.getName());
        responseDto.setBreed(cattle.getBreed());
        responseDto.setBirthday(cattle.getBirthday());
        responseDto.setWeight(cattle.getWeight());
        responseDto.setGender(cattle.getGender());
        responseDto.setImage(cattle.getImage());
        responseDto.setMotherId(cattle.getMotherId());
        responseDto.setFatherId(cattle.getFatherId());
        responseDto.setStatusId(cattle.getStatusId());
        responseDto.setOwnerId(cattle.getOwnerId());
        return responseDto;
    }
}
