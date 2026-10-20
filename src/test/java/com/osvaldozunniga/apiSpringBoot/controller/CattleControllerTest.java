package com.osvaldozunniga.apiSpringBoot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.osvaldozunniga.apiSpringBoot.model.dto.CattleRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.CattleResponseDto;
import com.osvaldozunniga.apiSpringBoot.service.CattleService;

@ExtendWith(MockitoExtension.class)
class CattleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CattleService cattleService;

    @InjectMocks
    private CattleController cattleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cattleController).build();
    }

    @Test
    void testCreateCattle_Json() throws Exception {
        CattleResponseDto responseDto = new CattleResponseDto();
        responseDto.setId("123");
        responseDto.setName("Lola");
        responseDto.setEarTag("TAG-001");

        when(cattleService.createCattle(any(CattleRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/cattle/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Lola\", \"earTag\": \"TAG-001\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lola"))
                .andExpect(jsonPath("$.earTag").value("TAG-001"));
    }

    @Test
    void testCreateCattle_MultipartWithImage() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "cow.jpg",
                "image/jpeg",
                "test-image-data".getBytes()
        );

        CattleResponseDto responseDto = new CattleResponseDto();
        responseDto.setId("123");
        responseDto.setName("Lola");
        responseDto.setEarTag("TAG-002");
        responseDto.setImage("/uploads/random-uuid.jpg");

        when(cattleService.createCattle(any(CattleRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(multipart("/api/cattle/")
                .file(imageFile)
                .param("name", "Lola")
                .param("earTag", "TAG-002"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lola"))
                .andExpect(jsonPath("$.image").value("/uploads/random-uuid.jpg"));
    }

    @Test
    void testUploadCattleImage() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "cow-updated.jpg",
                "image/jpeg",
                "new-image-data".getBytes()
        );

        CattleResponseDto responseDto = new CattleResponseDto();
        responseDto.setId("123");
        responseDto.setName("Lola");
        responseDto.setImage("/uploads/new-uuid.jpg");

        when(cattleService.uploadCattleImage(eq("123"), any())).thenReturn(responseDto);

        mockMvc.perform(multipart("/api/cattle/123/image")
                .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image").value("/uploads/new-uuid.jpg"));
    }

    @Test
    void testUploadImageOnly() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                "png-data".getBytes()
        );

        when(cattleService.uploadImageOnly(any())).thenReturn("/uploads/standalone.png");

        mockMvc.perform(multipart("/api/cattle/upload-image")
                .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value("/uploads/standalone.png"));
    }
}
