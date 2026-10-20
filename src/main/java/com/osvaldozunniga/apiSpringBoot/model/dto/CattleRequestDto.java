package com.osvaldozunniga.apiSpringBoot.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CattleRequestDto {
    private String earTag;
    private String name;
    private String breed;
    private String birthday;
    private Double weight;
    private String gender;
    private String motherId;
    private String fatherId;
    private Integer statusId;
    private String ownerId;
    private MultipartFile image;
    private MultipartFile imageFile;
    private MultipartFile file;

    public CattleRequestDto() {
    }

    public MultipartFile getEffectiveImageFile() {
        if (image != null && !image.isEmpty()) {
            return image;
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            return imageFile;
        }
        if (file != null && !file.isEmpty()) {
            return file;
        }
        return null;
    }
}
