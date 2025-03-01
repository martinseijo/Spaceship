package com.martinseijo.spaceship.application.service;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.application.mapper.SpaceshipMapper;
import com.martinseijo.spaceship.domain.exception.InvalidSpaceshipException;
import com.martinseijo.spaceship.domain.exception.PaginationException;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import com.martinseijo.spaceship.domain.model.Spaceship;
import com.martinseijo.spaceship.domain.repository.SpaceshipRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
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
class SpaceshipServiceImplTest {

    @Autowired
    private SpaceshipServiceImpl spaceshipService;

    @MockitoBean
    private SpaceshipRepository repository;

    @Spy
    private SpaceshipMapper spaceshipMapper;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testGetAllSpaceships() {
        Spaceship spaceship1 = Instancio.create(Spaceship.class);
        Spaceship spaceship2 = Instancio.create(Spaceship.class);
        List<Spaceship> spaceships = List.of(spaceship1, spaceship2);

        when(repository.findAll()).thenReturn(spaceships);

        List<SpaceshipDTO> result = spaceshipService.getAllSpaceships();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo(spaceship1.getName());
        assertThat(result.get(0).getId()).isEqualTo(spaceship1.getId());
        assertThat(result.get(1).getName()).isEqualTo(spaceship2.getName());
        assertThat(result.get(1).getId()).isEqualTo(spaceship2.getId());
    }

    @Test
    void testGetAllSpaceshipsPaginated() {
        Pageable pageable = PageRequest.of(0, 2);
        Spaceship spaceship1 = Instancio.create(Spaceship.class);
        Spaceship spaceship2 = Instancio.create(Spaceship.class);
        List<Spaceship> spaceships = List.of(spaceship1, spaceship2);

        Page<Spaceship> spaceshipPage = new PageImpl<>(spaceships, pageable, 2);

        when(repository.findAll(pageable)).thenReturn(spaceshipPage);

        Page<SpaceshipDTO> result = spaceshipService.getAllSpaceshipsPaginated(pageable);
        List<SpaceshipDTO> spaceshipsResult = result.stream().toList();

        assertThat(result.getTotalElements()).isEqualTo(spaceships.size());
        assertThat(spaceshipsResult.get(0).getId()).isEqualTo(spaceships.get(0).getId());
        assertThat(spaceshipsResult.get(0).getName()).isEqualTo(spaceships.get(0).getName());
        assertThat(spaceshipsResult.get(1).getId()).isEqualTo(spaceships.get(1).getId());
        assertThat(spaceshipsResult.get(1).getName()).isEqualTo(spaceships.get(1).getName());
    }

    @Test
    void testGetAllSpaceshipsPaginated_Exception() {
        Pageable pageable = PageRequest.of(0, 2);

        when(repository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> spaceshipService.getAllSpaceshipsPaginated(pageable))
                .isInstanceOf(PaginationException.class)
                .hasMessageContaining("Error retrieving paginated spaceships");
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
    void testGetSpaceshipsByFilter() {
        SpaceshipFilter filter = new SpaceshipFilter();
        filter.setName("Enterprise");
        Pageable pageable = PageRequest.of(0, 2);
        when(repository.findByNameContainingIgnoreCase("Enterprise", pageable)).thenReturn(new PageImpl<>(Collections.singletonList(new Spaceship())));

        Page<SpaceshipDTO> result = spaceshipService.getSpaceshipsByFilter(filter, pageable);

        assertThat(result).isNotEmpty();
        verify(repository, times(1)).findByNameContainingIgnoreCase("Enterprise", pageable);
    }

    @Test
    void testGetSpaceshipsByFilter_Exception() {
        SpaceshipFilter filter = new SpaceshipFilter();
        filter.setName("Enterprise");
        Pageable pageable = PageRequest.of(0, 2);

        when(repository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> spaceshipService.getSpaceshipsByFilter(filter, pageable))
                .isInstanceOf(PaginationException.class)
                .hasMessageContaining("Error retrieving paginated spaceships");
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
    void testCreateWithNullName() {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setName(null);

        assertThatThrownBy(() -> spaceshipService.create(dto))
                .isInstanceOf(InvalidSpaceshipException.class)
                .hasMessageContaining("Spaceship name cannot be null or empty");
    }

    @Test
    void testCreateWithEmptyName() {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setName("");

        assertThatThrownBy(() -> spaceshipService.create(dto))
                .isInstanceOf(InvalidSpaceshipException.class)
                .hasMessageContaining("Spaceship name cannot be null or empty");
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
    void testUpdateWithOutName() throws ResourceNotFoundException {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setId(1L);
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("Enterprise");
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
