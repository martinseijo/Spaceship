package com.martinseijo.spaceship.application.service;

import com.martinseijo.spaceship.domain.model.Spaceship;
import com.martinseijo.spaceship.domain.repository.SpaceshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceshipService {

    private final SpaceshipRepository repository;

    public List<Spaceship> getAllSpaceships() {
        return repository.findAll();
    }
}
