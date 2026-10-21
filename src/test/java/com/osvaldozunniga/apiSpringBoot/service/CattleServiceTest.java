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
import org.springframework.mock.web.MockMultipartFile;

import com.osvaldozunniga.apiSpringBoot.exception.ResourceNotFoundException;
import com.osvaldozunniga.apiSpringBoot.model.dto.CattleRequestDto;
import com.osvaldozunniga.apiSpringBoot.model.dto.CattleResponseDto;
import com.osvaldozunniga.apiSpringBoot.model.entity.Cattle;
import com.osvaldozunniga.apiSpringBoot.repository.CattleRepository;

@ExtendWith(MockitoExtension.class)
class CattleServiceTest {

    @Mock
    private CattleRepository cattleRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CattleService cattleService;

    @Test
    void testGetAllCattles() {
        Cattle cattle = new Cattle();
        cattle.setId("cat-1");
        cattle.setName("Palomo");

        when(cattleRepository.findAll()).thenReturn(Collections.singletonList(cattle));

        List<CattleResponseDto> resultList = cattleService.getAllCattles();

        assertEquals(1, resultList.size());
        assertEquals("Palomo", resultList.get(0).getName());
    }

    @Test
    void testGetCattleById_Success() {
        Cattle cattle = new Cattle();
        cattle.setId("cat-1");
        cattle.setName("Palomo");

        when(cattleRepository.findById("cat-1")).thenReturn(Optional.of(cattle));

        CattleResponseDto resultDto = cattleService.getCattleById("cat-1");

        assertNotNull(resultDto);
        assertEquals("cat-1", resultDto.getId());
        assertEquals("Palomo", resultDto.getName());
    }

    @Test
    void testGetCattleById_NotFound() {
        when(cattleRepository.findById("unknown-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cattleService.getCattleById("unknown-id");
        });
    }

    @Test
    void testCreateCattle_Success() {
        CattleRequestDto requestDto = new CattleRequestDto();
        requestDto.setName("Lola");
        requestDto.setBreed("Brahman");

        Cattle savedCattle = new Cattle();
        savedCattle.setId("cat-new");
        savedCattle.setName("Lola");
        savedCattle.setBreed("Brahman");
        savedCattle.setEarTag("CRIA-12345");

        when(cattleRepository.save(any(Cattle.class))).thenReturn(savedCattle);

        CattleResponseDto resultDto = cattleService.createCattle(requestDto);

        assertNotNull(resultDto);
        assertEquals("cat-new", resultDto.getId());
        assertEquals("Lola", resultDto.getName());
    }

    @Test
    void testDestroyCattle_Success() {
        Cattle existingCattle = new Cattle();
        existingCattle.setId("cat-1");
        existingCattle.setStatusId(1);

        when(cattleRepository.findById("cat-1")).thenReturn(Optional.of(existingCattle));
        when(cattleRepository.save(any(Cattle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CattleResponseDto resultDto = cattleService.destroyCattle("cat-1");

        assertEquals(0, resultDto.getStatusId());
        verify(cattleRepository).save(existingCattle);
    }

    @Test
    void testUpdateCattleStatus_Success() {
        Cattle existingCattle = new Cattle();
        existingCattle.setId("cat-1");
        existingCattle.setStatusId(1);

        when(cattleRepository.findById("cat-1")).thenReturn(Optional.of(existingCattle));
        when(cattleRepository.save(any(Cattle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CattleResponseDto resultDto = cattleService.updateCattleStatus("cat-1", 2);

        assertEquals(2, resultDto.getStatusId());
        verify(cattleRepository).save(existingCattle);
    }
}
