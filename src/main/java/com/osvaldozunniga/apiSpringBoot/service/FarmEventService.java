package com.osvaldozunniga.apiSpringBoot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.osvaldozunniga.apiSpringBoot.exception.ResourceNotFoundException;
import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventResponseDto;
import com.osvaldozunniga.apiSpringBoot.model.entity.FarmEvent;
import com.osvaldozunniga.apiSpringBoot.repository.FarmEventRepository;

@Service
public class FarmEventService {

    private static final Logger logger = LoggerFactory.getLogger(FarmEventService.class);

    private final FarmEventRepository farmEventRepository;

    public FarmEventService(FarmEventRepository farmEventRepository) {
        this.farmEventRepository = farmEventRepository;
    }

    public List<FarmEventResponseDto> getAllEvents() {
        return farmEventRepository.findAll().stream()
                .map(FarmEventResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public FarmEventResponseDto getEventById(String farmEventId) {
        FarmEvent farmEvent = farmEventRepository.findById(farmEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + farmEventId));
        return FarmEventResponseDto.fromEntity(farmEvent);
    }

    public FarmEventResponseDto createEvent(FarmEventRequestDto farmEventRequest) {
        FarmEvent farmEvent = new FarmEvent();
        farmEvent.setType(farmEventRequest.getType());
        farmEvent.setDescription(farmEventRequest.getDescription());
        farmEvent.setDate(farmEventRequest.getDate());

        FarmEvent savedFarmEvent = farmEventRepository.save(farmEvent);
        logger.info("Created new farm event with ID: {}", savedFarmEvent.getId());
        return FarmEventResponseDto.fromEntity(savedFarmEvent);
    }

    public FarmEventResponseDto updateEvent(String farmEventId, FarmEventRequestDto farmEventRequest) {
        FarmEvent existingFarmEvent = farmEventRepository.findById(farmEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + farmEventId));

        if (farmEventRequest.getType() != null) {
            existingFarmEvent.setType(farmEventRequest.getType());
        }
        if (farmEventRequest.getDescription() != null) {
            existingFarmEvent.setDescription(farmEventRequest.getDescription());
        }
        if (farmEventRequest.getDate() != null) {
            existingFarmEvent.setDate(farmEventRequest.getDate());
        }

        FarmEvent updatedFarmEvent = farmEventRepository.save(existingFarmEvent);
        logger.info("Updated farm event with ID: {}", updatedFarmEvent.getId());
        return FarmEventResponseDto.fromEntity(updatedFarmEvent);
    }

    public void deleteEvent(String farmEventId) {
        FarmEvent existingFarmEvent = farmEventRepository.findById(farmEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + farmEventId));

        farmEventRepository.delete(existingFarmEvent);
        logger.info("Deleted farm event with ID: {}", farmEventId);
    }
}
