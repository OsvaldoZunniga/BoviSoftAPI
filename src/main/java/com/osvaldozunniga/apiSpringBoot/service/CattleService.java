package com.osvaldozunniga.apiSpringBoot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.osvaldozunniga.apiSpringBoot.exception.ResourceNotFoundException;
import com.osvaldozunniga.apiSpringBoot.model.dto.CattleRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.CattleResponseDto;
import com.osvaldozunniga.apiSpringBoot.model.entity.Cattle;
import com.osvaldozunniga.apiSpringBoot.repository.CattleRepository;

@Service
public class CattleService {

    private static final Logger logger = LoggerFactory.getLogger(CattleService.class);

    private final CattleRepository cattleRepository;
    private final FileStorageService fileStorageService;

    public CattleService(CattleRepository cattleRepository, FileStorageService fileStorageService) {
        this.cattleRepository = cattleRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<CattleResponseDto> getAllCattles() {
        return cattleRepository.findAll().stream()
                .map(CattleResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CattleResponseDto getCattleById(String cattleId) {
        Cattle cattle = cattleRepository.findById(cattleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bovino no encontrado con ID: " + cattleId));
        return CattleResponseDto.fromEntity(cattle);
    }

    public CattleResponseDto createCattle(CattleRequestDto cattleRequest) {
        Cattle cattle = new Cattle();

        String resolvedEarTag = (cattleRequest.getEarTag() != null && !cattleRequest.getEarTag().trim().isEmpty())
                ? cattleRequest.getEarTag().trim()
                : "CRIA-" + (System.currentTimeMillis() % 100000);
        cattle.setEarTag(resolvedEarTag);

        cattle.setName(cattleRequest.getName());
        cattle.setBreed(cattleRequest.getBreed() != null && !cattleRequest.getBreed().trim().isEmpty()
                ? cattleRequest.getBreed().trim()
                : "Brahman");
        cattle.setBirthday(cattleRequest.getBirthday());
        cattle.setWeight(cattleRequest.getWeight() != null ? cattleRequest.getWeight() : 35.0);
        cattle.setGender(cattleRequest.getGender() != null && !cattleRequest.getGender().trim().isEmpty()
                ? cattleRequest.getGender().trim()
                : "Hembra");
        cattle.setMotherId(cattleRequest.getMotherId() != null && !cattleRequest.getMotherId().trim().isEmpty()
                ? cattleRequest.getMotherId().trim()
                : "none");
        cattle.setFatherId(cattleRequest.getFatherId() != null && !cattleRequest.getFatherId().trim().isEmpty()
                ? cattleRequest.getFatherId().trim()
                : "none");
        cattle.setOwnerId(cattleRequest.getOwnerId());
        cattle.setStatusId(cattleRequest.getStatusId() != null ? cattleRequest.getStatusId() : 1);

        MultipartFile imageFile = cattleRequest.getEffectiveImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String storedImagePath = fileStorageService.storeFile(imageFile);
            cattle.setImage(storedImagePath);
        }

        Cattle savedCattle = cattleRepository.save(cattle);
        logger.info("Created new cattle record with ID: {} and earTag: {}", savedCattle.getId(), savedCattle.getEarTag());
        return CattleResponseDto.fromEntity(savedCattle);
    }

    public CattleResponseDto updateCattle(String cattleId, CattleRequestDto cattleRequest) {
        Cattle existingCattle = cattleRepository.findById(cattleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bovino no encontrado con ID: " + cattleId));

        if (cattleRequest.getName() != null) {
            existingCattle.setName(cattleRequest.getName());
        }
        if (cattleRequest.getBirthday() != null) {
            existingCattle.setBirthday(cattleRequest.getBirthday());
        }
        if (cattleRequest.getBreed() != null) {
            existingCattle.setBreed(cattleRequest.getBreed());
        }
        if (cattleRequest.getEarTag() != null) {
            existingCattle.setEarTag(cattleRequest.getEarTag());
        }
        if (cattleRequest.getWeight() != null) {
            existingCattle.setWeight(cattleRequest.getWeight());
        }
        if (cattleRequest.getGender() != null) {
            existingCattle.setGender(cattleRequest.getGender());
        }
        if (cattleRequest.getMotherId() != null) {
            existingCattle.setMotherId(cattleRequest.getMotherId());
        }
        if (cattleRequest.getFatherId() != null) {
            existingCattle.setFatherId(cattleRequest.getFatherId());
        }
        if (cattleRequest.getStatusId() != null) {
            existingCattle.setStatusId(cattleRequest.getStatusId());
        }
        if (cattleRequest.getOwnerId() != null) {
            existingCattle.setOwnerId(cattleRequest.getOwnerId());
        }

        MultipartFile newImageFile = cattleRequest.getEffectiveImageFile();
        if (newImageFile != null && !newImageFile.isEmpty()) {
            if (existingCattle.getImage() != null) {
                fileStorageService.deleteFile(existingCattle.getImage());
            }
            String storedImagePath = fileStorageService.storeFile(newImageFile);
            existingCattle.setImage(storedImagePath);
        }

        Cattle updatedCattle = cattleRepository.save(existingCattle);
        logger.info("Updated cattle record with ID: {}", updatedCattle.getId());
        return CattleResponseDto.fromEntity(updatedCattle);
    }

    public CattleResponseDto uploadCattleImage(String cattleId, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("El archivo de imagen no puede estar vacío");
        }

        Cattle existingCattle = cattleRepository.findById(cattleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bovino no encontrado con ID: " + cattleId));

        if (existingCattle.getImage() != null) {
            fileStorageService.deleteFile(existingCattle.getImage());
        }

        String storedImagePath = fileStorageService.storeFile(imageFile);
        existingCattle.setImage(storedImagePath);

        Cattle updatedCattle = cattleRepository.save(existingCattle);
        logger.info("Updated image for cattle record with ID: {}", updatedCattle.getId());
        return CattleResponseDto.fromEntity(updatedCattle);
    }

    public String uploadImageOnly(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("No se proporcionó archivo de imagen");
        }
        return fileStorageService.storeFile(imageFile);
    }

    public CattleResponseDto destroyCattle(String cattleId) {
        Cattle existingCattle = cattleRepository.findById(cattleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bovino no encontrado con ID: " + cattleId));

        existingCattle.setStatusId(0);
        Cattle updatedCattle = cattleRepository.save(existingCattle);
        logger.info("Logically deleted cattle record with ID: {}", updatedCattle.getId());
        return CattleResponseDto.fromEntity(updatedCattle);
    }

    public CattleResponseDto updateCattleStatus(String cattleId, int statusId) {
        Cattle existingCattle = cattleRepository.findById(cattleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bovino no encontrado con ID: " + cattleId));

        existingCattle.setStatusId(statusId);
        Cattle updatedCattle = cattleRepository.save(existingCattle);
        logger.info("Updated status to {} for cattle record with ID: {}", statusId, updatedCattle.getId());
        return CattleResponseDto.fromEntity(updatedCattle);
    }
}
