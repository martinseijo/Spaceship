package com.martinseijo.spaceship.application.service;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.application.mapper.SpaceshipMapper;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import com.martinseijo.spaceship.domain.model.Spaceship;
import com.martinseijo.spaceship.domain.repository.SpaceshipRepository;
import com.martinseijo.spaceship.domain.service.SpaceshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceshipServiceImpl implements SpaceshipService {

    private static final String SPACESHIP_NOT_FOUND = "Spaceship not found with id ";

    private final SpaceshipRepository repository;
    private final SpaceshipMapper mapper;

    @Override
    @Cacheable("spaceships")
    public List<SpaceshipDTO> getAllSpaceships() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Cacheable("spaceshipsPaginated")
    public Page<SpaceshipDTO> getAllSpaceshipsPaginated(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    @Cacheable("spaceship")
    public SpaceshipDTO getById(Long id) throws ResourceNotFoundException {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SPACESHIP_NOT_FOUND + id)));
    }

    @Override
    public List<SpaceshipDTO> getSpaceshipsByFilter(SpaceshipFilter filter) {
        return mapper.toDTOList(repository.findByNameContainingIgnoreCase(filter.getName()));
    }

    @Override
    public SpaceshipDTO create(SpaceshipDTO dto) {
        Spaceship entity = Spaceship.builder()
                .name(dto.getName())
                .build();
        repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public SpaceshipDTO update(SpaceshipDTO dto) throws ResourceNotFoundException {
        Spaceship entity = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(SPACESHIP_NOT_FOUND + dto.getId()));
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public SpaceshipDTO delete(Long id) throws ResourceNotFoundException {
        Spaceship entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SPACESHIP_NOT_FOUND + id));
        repository.delete(entity);
        return mapper.toDTO(entity);
    }
}