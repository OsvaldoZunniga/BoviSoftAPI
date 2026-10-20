package com.osvaldozunniga.apiSpringBoot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.osvaldozunniga.apiSpringBoot.exception.ResourceNotFoundException;
import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventResponseDto;
import com.osvaldozunniga.apiSpringBoot.model.entity.FarmEvent;
import com.osvaldozunniga.apiSpringBoot.repository.FarmEventRepository;

@ExtendWith(MockitoExtension.class)
class FarmEventServiceTest {

    @Mock
    private FarmEventRepository farmEventRepository;

    @InjectMocks
    private FarmEventService farmEventService;

    @Test
    void testGetAllEvents() {
        FarmEvent event = new FarmEvent();
        event.setId("evt-1");
        event.setType("Vacuna");

        when(farmEventRepository.findAll()).thenReturn(Collections.singletonList(event));

        List<FarmEventResponseDto> resultList = farmEventService.getAllEvents();

        assertEquals(1, resultList.size());
        assertEquals("Vacuna", resultList.get(0).getType());
    }

    @Test
    void testGetEventById_Success() {
        FarmEvent event = new FarmEvent();
        event.setId("evt-1");
        event.setType("Vacuna");

        when(farmEventRepository.findById("evt-1")).thenReturn(Optional.of(event));

        FarmEventResponseDto resultDto = farmEventService.getEventById("evt-1");

        assertNotNull(resultDto);
        assertEquals("evt-1", resultDto.getId());
        assertEquals("Vacuna", resultDto.getType());
    }

    @Test
    void testGetEventById_NotFound() {
        when(farmEventRepository.findById("unknown-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            farmEventService.getEventById("unknown-id");
        });
    }

    @Test
    void testCreateEvent_Success() {
        FarmEventRequestDto requestDto = new FarmEventRequestDto();
        requestDto.setType("Parto");
        requestDto.setDate("2026-10-10");

        FarmEvent savedEvent = new FarmEvent();
        savedEvent.setId("evt-new");
        savedEvent.setType("Parto");
        savedEvent.setDate("2026-10-10");

        when(farmEventRepository.save(any(FarmEvent.class))).thenReturn(savedEvent);

        FarmEventResponseDto resultDto = farmEventService.createEvent(requestDto);

        assertNotNull(resultDto);
        assertEquals("evt-new", resultDto.getId());
        assertEquals("Parto", resultDto.getType());
    }

    @Test
    void testDeleteEvent_Success() {
        FarmEvent existingEvent = new FarmEvent();
        existingEvent.setId("evt-1");

        when(farmEventRepository.findById("evt-1")).thenReturn(Optional.of(existingEvent));

        farmEventService.deleteEvent("evt-1");

        verify(farmEventRepository).delete(existingEvent);
    }
}
