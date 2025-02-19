package com.martinseijo.spaceship.infrastructure.web;

import com.martinseijo.spaceship.application.service.SpaceshipService;
import com.martinseijo.spaceship.domain.model.Spaceship;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

    private final SpaceshipService spaceshipService;

    @GetMapping
    public List<Spaceship> getAllSpaceships() {
        return spaceshipService.getAllSpaceships();
    }
}
