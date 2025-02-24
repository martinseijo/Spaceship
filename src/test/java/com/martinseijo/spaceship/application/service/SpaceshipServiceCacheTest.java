package com.martinseijo.spaceship.application.service;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpaceshipServiceCacheTest {

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
        SpaceshipDTO spaceship = new SpaceshipDTO();
        spaceship.setId(1L);
        spaceship.setName("Enterprise");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(new Spaceship()));

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
}
