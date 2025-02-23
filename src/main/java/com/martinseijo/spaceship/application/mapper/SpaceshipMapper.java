package com.martinseijo.spaceship.application.mapper;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.domain.model.Spaceship;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpaceshipMapper {

    SpaceshipDTO toDTO(Spaceship spaceship);

    Spaceship toEntity(SpaceshipDTO dto);

    List<SpaceshipDTO> toDTOList(List<Spaceship> spaceships);

    List<Spaceship> toEntityList(List<SpaceshipDTO> dtos);

}
