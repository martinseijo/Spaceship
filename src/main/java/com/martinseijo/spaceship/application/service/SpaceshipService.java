package com.martinseijo.spaceship.application.service;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.application.mapper.SpaceshipMapper;
import com.martinseijo.spaceship.domain.model.Spaceship;
import com.martinseijo.spaceship.domain.repository.SpaceshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceshipService {

    private final SpaceshipRepository repository;

    private final SpaceshipMapper mapper;

    @Cacheable("spaceships")
    public List<SpaceshipDTO> getAllSpaceships() {
        return mapper.toDTOList(repository.findAll());
    }

    @Cacheable("spaceshipsPaginated")
    public Page<SpaceshipDTO> getAllSpaceshipsPaginated(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Cacheable("spaceship")
    public SpaceshipDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id).orElseThrow());
    }

    public List<SpaceshipDTO> getSpaceshipsByFilter(SpaceshipFilter filter) {
        return mapper.toDTOList(repository.findByNameContainingIgnoreCase(filter.getName()));
    }

    public SpaceshipDTO create(SpaceshipDTO dto) {
        Spaceship entity = Spaceship.builder()
                .name(dto.getName())
                .build();
        repository.save(entity);
        return mapper.toDTO(entity);
    }

    public SpaceshipDTO update(SpaceshipDTO dto) {
        Spaceship entity = repository.findById(dto.getId()).orElseThrow();
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        repository.save(entity);
        return mapper.toDTO(entity);
    }

    public SpaceshipDTO delete(Long id) {
        Spaceship entity = repository.findById(id).orElseThrow();
        repository.delete(entity);
        return mapper.toDTO(entity);
    }
}
