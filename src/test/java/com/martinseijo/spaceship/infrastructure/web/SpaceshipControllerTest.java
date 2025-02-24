package com.martinseijo.spaceship.infrastructure.web;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.application.service.SpaceshipService;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpaceshipControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private SpaceshipService spaceshipService;

    @Test
    void testGetAllSpaceships() {
        when(spaceshipService.getAllSpaceships()).thenReturn(Collections.singletonList(new SpaceshipDTO()));

        ResponseEntity<SpaceshipDTO[]> response = restTemplate.getForEntity(createURLWithPort("/spaceships"), SpaceshipDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetById() throws ResourceNotFoundException {
        SpaceshipDTO spaceship = new SpaceshipDTO();
        spaceship.setId(1L);
        when(spaceshipService.getById(1L)).thenReturn(spaceship);

        ResponseEntity<SpaceshipDTO> response = restTemplate.getForEntity(createURLWithPort("/spaceships/1"), SpaceshipDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetByIdNotFound() throws ResourceNotFoundException {
        when(spaceshipService.getById(1L)).thenThrow(new ResourceNotFoundException("Spaceship not found with id 1"));

        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/spaceships/1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetSpaceshipsByFilter() {
        SpaceshipFilter filter = new SpaceshipFilter();
        filter.setName("Enterprise");
        when(spaceshipService.getSpaceshipsByFilter(any())).thenReturn(Collections.singletonList(new SpaceshipDTO()));

        ResponseEntity<SpaceshipDTO[]> response = restTemplate.postForEntity(createURLWithPort("/spaceships/search"), filter, SpaceshipDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testCreate() {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setName("Enterprise");
        when(spaceshipService.create(any())).thenReturn(dto);

        ResponseEntity<SpaceshipDTO> response = restTemplate.postForEntity(createURLWithPort("/spaceships/create"), dto, SpaceshipDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUpdate() throws ResourceNotFoundException {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setId(1L);
        dto.setName("Enterprise");
        when(spaceshipService.update(any())).thenReturn(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SpaceshipDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<SpaceshipDTO> response = restTemplate.exchange(
                createURLWithPort("/spaceships/update"),
                HttpMethod.PUT,
                request,
                SpaceshipDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(spaceshipService, times(1)).update(any());
    }

    @Test
    void testDelete() throws ResourceNotFoundException {
        SpaceshipDTO dto = new SpaceshipDTO();
        dto.setId(1L);
        when(spaceshipService.delete(1L)).thenReturn(dto);

        ResponseEntity<SpaceshipDTO> response = restTemplate.exchange(
                createURLWithPort("/spaceships/delete/1"),
                HttpMethod.DELETE,
                null,
                SpaceshipDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(spaceshipService, times(1)).delete(1L);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}