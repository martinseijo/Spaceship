package com.martinseijo.spaceship.infrastructure.web;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SpaceshipController {
    ResponseEntity<List<SpaceshipDTO>> getAllSpaceships();
    ResponseEntity<SpaceshipDTO> getById(Long id) throws ResourceNotFoundException;
    ResponseEntity<Page<SpaceshipDTO>> getAllSpaceshipsPaginated(Pageable pageable);
    ResponseEntity<Page<SpaceshipDTO>> getSpaceshipsByFilter(SpaceshipFilter filter, Pageable pageable);
    ResponseEntity<SpaceshipDTO> create(SpaceshipDTO spaceshipDTO);
    ResponseEntity<SpaceshipDTO> update(SpaceshipDTO spaceshipDTO) throws ResourceNotFoundException;
    ResponseEntity<Void> deleteById(Long id) throws ResourceNotFoundException;
}
