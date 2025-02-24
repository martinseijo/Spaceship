package com.martinseijo.spaceship.application.service;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import com.martinseijo.spaceship.domain.model.Spaceship;
import com.martinseijo.spaceship.domain.repository.SpaceshipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpaceshipServiceTest {

    @Autowired
    private SpaceshipService spaceshipService;

    @MockitoBean
    private SpaceshipRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testGetAllSpaceshipsCaching() {
        when(repository.findAll()).thenReturn(Collections.singletonList(new Spaceship()));

        List<SpaceshipDTO> firstCall = spaceshipService.getAllSpaceships();
        List<SpaceshipDTO> secondCall = spaceshipService.getAllSpaceships();

        assertThat(firstCall).isEqualTo(secondCall);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetByIdCaching() throws ResourceNotFoundException {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(spaceship));

        SpaceshipDTO firstCall = spaceshipService.getById(1L);
        SpaceshipDTO secondCall = spaceshipService.getById(1L);

        assertThat(firstCall).isEqualTo(secondCall);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetAllSpaceshipsPaginatedCaching() {
        final Pageable pageable = PageRequest.of(0, 2);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(new Spaceship())));

        Page<SpaceshipDTO> firstCall = spaceshipService.getAllSpaceshipsPaginated(pageable);
        Page<SpaceshipDTO> secondCall = spaceshipService.getAllSpaceshipsPaginated(pageable);

        assertThat(firstCall).isEqualTo(secondCall);
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testGetSpaceshipsByFilter() {
        SpaceshipFilter filter = new SpaceshipFilter();
        filter.setName("Enterprise");
        when(repository.findByNameContainingIgnoreCase("Enterprise")).thenReturn(Collections.singletonList(new Spaceship()));

        List<SpaceshipDTO> result = spaceshipService.getSpaceshipsByFilter(filter);

        assertThat(result).isNotEmpty();
        verify(repository, times(1)).findByNameContainingIgnoreCase("Enterprise");
    }

    @Test
    void testCreate() {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setName("Enterprise");
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        when(repository.save(any(Spaceship.class))).thenReturn(spaceship);

        SpaceshipDTO result = spaceshipService.create(dto);

        assertThat(result.getName()).isEqualTo("Enterprise");
        verify(repository, times(1)).save(any(Spaceship.class));
    }

    @Test
    void testUpdate() throws ResourceNotFoundException {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setId(1L);
        dto.setName("Enterprise");
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(spaceship));
        when(repository.save(any(Spaceship.class))).thenReturn(spaceship);

        SpaceshipDTO result = spaceshipService.update(dto);

        assertThat(result.getName()).isEqualTo("Enterprise");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Spaceship.class));
    }

    @Test
    void testUpdateNotFound() {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spaceshipService.update(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Spaceship not found with id 1");
    }

    @Test
    void testDelete() throws ResourceNotFoundException {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(spaceship));

        SpaceshipDTO result = spaceshipService.delete(1L);

        assertThat(result).isNotNull();
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(spaceship);
    }

    @Test
    void testDeleteNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spaceshipService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Spaceship not found with id 1");
    }
}
