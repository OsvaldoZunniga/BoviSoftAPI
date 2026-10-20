package com.osvaldozunniga.apiSpringBoot.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.osvaldozunniga.apiSpringBoot.model.dto.CattleRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.CattleResponseDto;
import com.osvaldozunniga.apiSpringBoot.service.CattleService;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://127.0.0.1:3000" })
@RequestMapping({"/api/cattle", "/cattle"})
public class CattleController {

    private final CattleService cattleService;

    public CattleController(CattleService cattleService) {
        this.cattleService = cattleService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<CattleResponseDto>> getAllCattles() {
        List<CattleResponseDto> cattleList = cattleService.getAllCattles();
        return ResponseEntity.ok(cattleList);
    }

    @GetMapping("/{cattleId}")
    public ResponseEntity<CattleResponseDto> getCattleById(@PathVariable String cattleId) {
        CattleResponseDto cattleResponse = cattleService.getCattleById(cattleId);
        return ResponseEntity.ok(cattleResponse);
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CattleResponseDto> createCattleFromJson(@RequestBody CattleRequestDto cattleRequest) {
        CattleResponseDto createdCattle = cattleService.createCattle(cattleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCattle);
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CattleResponseDto> createCattleFromFormData(@ModelAttribute CattleRequestDto cattleRequest) {
        CattleResponseDto createdCattle = cattleService.createCattle(cattleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCattle);
    }

    @PutMapping(value = "/{cattleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CattleResponseDto> updateCattleFromJson(
            @PathVariable String cattleId,
            @RequestBody CattleRequestDto cattleRequest) {
        CattleResponseDto updatedCattle = cattleService.updateCattle(cattleId, cattleRequest);
        return ResponseEntity.ok(updatedCattle);
    }

    @PutMapping(value = "/{cattleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CattleResponseDto> updateCattleFromFormData(
            @PathVariable String cattleId,
            @ModelAttribute CattleRequestDto cattleRequest) {
        CattleResponseDto updatedCattle = cattleService.updateCattle(cattleId, cattleRequest);
        return ResponseEntity.ok(updatedCattle);
    }

    @PostMapping(value = "/{cattleId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CattleResponseDto> uploadCattleImage(
            @PathVariable String cattleId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        MultipartFile effectiveMultipartFile = (image != null && !image.isEmpty()) ? image
                : ((imageFile != null && !imageFile.isEmpty()) ? imageFile : file);

        CattleResponseDto updatedCattle = cattleService.uploadCattleImage(cattleId, effectiveMultipartFile);
        return ResponseEntity.ok(updatedCattle);
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImageOnly(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        MultipartFile effectiveMultipartFile = (image != null && !image.isEmpty()) ? image
                : ((imageFile != null && !imageFile.isEmpty()) ? imageFile : file);

        String storedImageUrl = cattleService.uploadImageOnly(effectiveMultipartFile);
        return ResponseEntity.ok(Collections.singletonMap("imageUrl", storedImageUrl));
    }

    @DeleteMapping("/{cattleId}/destroy")
    public ResponseEntity<CattleResponseDto> destroyCattle(@PathVariable String cattleId) {
        CattleResponseDto deletedCattle = cattleService.destroyCattle(cattleId);
        return ResponseEntity.ok(deletedCattle);
    }
}
