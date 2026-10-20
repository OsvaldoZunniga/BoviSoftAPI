package com.osvaldozunniga.apiSpringBoot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventResponseDto;
import com.osvaldozunniga.apiSpringBoot.service.FarmEventService;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://127.0.0.1:3000" })
@RequestMapping({"/api/farm-events", "/farmEvents", "/api/farmEvents"})
public class FarmEventController {

    private final FarmEventService farmEventService;

    public FarmEventController(FarmEventService farmEventService) {
        this.farmEventService = farmEventService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<FarmEventResponseDto>> getAllEvents() {
        List<FarmEventResponseDto> eventList = farmEventService.getAllEvents();
        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/{farmEventId}")
    public ResponseEntity<FarmEventResponseDto> getEventById(@PathVariable String farmEventId) {
        FarmEventResponseDto eventResponse = farmEventService.getEventById(farmEventId);
        return ResponseEntity.ok(eventResponse);
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FarmEventResponseDto> createEvent(@RequestBody FarmEventRequestDto farmEventRequest) {
        FarmEventResponseDto createdEvent = farmEventService.createEvent(farmEventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping(value = "/{farmEventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FarmEventResponseDto> updateEvent(
            @PathVariable String farmEventId,
            @RequestBody FarmEventRequestDto farmEventRequest) {
        FarmEventResponseDto updatedEvent = farmEventService.updateEvent(farmEventId, farmEventRequest);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{farmEventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String farmEventId) {
        farmEventService.deleteEvent(farmEventId);
        return ResponseEntity.noContent().build();
    }
}
