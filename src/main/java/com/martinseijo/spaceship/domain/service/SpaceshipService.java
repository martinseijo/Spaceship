package com.martinseijo.spaceship.domain.service;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpaceshipService {
    List<SpaceshipDTO> getAllSpaceships();
    SpaceshipDTO getById(Long id) throws ResourceNotFoundException;
    Page<SpaceshipDTO> getAllSpaceshipsPaginated(Pageable pageable);
    Page<SpaceshipDTO> getSpaceshipsByFilter(SpaceshipFilter filter, Pageable pageable);
    SpaceshipDTO create(SpaceshipDTO spaceshipDTO);
    SpaceshipDTO update(SpaceshipDTO spaceshipDTO) throws ResourceNotFoundException;
    SpaceshipDTO delete(Long id) throws ResourceNotFoundException;
}
