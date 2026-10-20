package com.osvaldozunniga.apiSpringBoot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.FarmEventResponseDto;
import com.osvaldozunniga.apiSpringBoot.service.FarmEventService;

@ExtendWith(MockitoExtension.class)
class FarmEventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FarmEventService farmEventService;

    @InjectMocks
    private FarmEventController farmEventController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(farmEventController).build();
    }

    @Test
    void testGetAllEvents() throws Exception {
        FarmEventResponseDto eventOne = new FarmEventResponseDto();
        eventOne.setId("evt-1");
        eventOne.setType("Vacuna");
        eventOne.setDate("2026-09-08");
        eventOne.setDescription("Vacunación contra aftosa");

        FarmEventResponseDto eventTwo = new FarmEventResponseDto();
        eventTwo.setId("evt-2");
        eventTwo.setType("Parto");
        eventTwo.setDate("2026-09-07");
        eventTwo.setDescription("Nacimiento de ternero");

        when(farmEventService.getAllEvents()).thenReturn(Arrays.asList(eventOne, eventTwo));

        mockMvc.perform(get("/api/farm-events/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].type").value("Vacuna"))
                .andExpect(jsonPath("$[1].type").value("Parto"));
    }

    @Test
    void testGetEventById() throws Exception {
        FarmEventResponseDto eventResponse = new FarmEventResponseDto();
        eventResponse.setId("evt-1");
        eventResponse.setType("Tratamiento");
        eventResponse.setDate("2026-09-08");
        eventResponse.setDescription("Tratamiento con vitaminas");

        when(farmEventService.getEventById("evt-1")).thenReturn(eventResponse);

        mockMvc.perform(get("/api/farm-events/evt-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("evt-1"))
                .andExpect(jsonPath("$.type").value("Tratamiento"));
    }

    @Test
    void testCreateEvent() throws Exception {
        FarmEventResponseDto savedEvent = new FarmEventResponseDto();
        savedEvent.setId("evt-new");
        savedEvent.setType("Vacuna");
        savedEvent.setDate("2026-09-08");
        savedEvent.setDescription("Vacunación general");

        when(farmEventService.createEvent(any(FarmEventRequestDto.class))).thenReturn(savedEvent);

        mockMvc.perform(post("/api/farm-events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\": \"Vacuna\", \"date\": \"2026-09-08\", \"description\": \"Vacunación general\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("evt-new"))
                .andExpect(jsonPath("$.type").value("Vacuna"))
                .andExpect(jsonPath("$.date").value("2026-09-08"));
    }

    @Test
    void testUpdateEvent() throws Exception {
        FarmEventResponseDto updatedEvent = new FarmEventResponseDto();
        updatedEvent.setId("evt-1");
        updatedEvent.setType("Vacuna");
        updatedEvent.setDate("2026-09-08");
        updatedEvent.setDescription("Descripción actualizada");

        when(farmEventService.updateEvent(eq("evt-1"), any(FarmEventRequestDto.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/api/farm-events/evt-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\": \"Descripción actualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Descripción actualizada"));
    }

    @Test
    void testDeleteEvent() throws Exception {
        doNothing().when(farmEventService).deleteEvent("evt-1");

        mockMvc.perform(delete("/api/farm-events/evt-1"))
                .andExpect(status().isNoContent());
    }
}
