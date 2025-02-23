package com.martinseijo.spaceship.infrastructure.web;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.application.service.SpaceshipService;
import com.martinseijo.spaceship.domain.model.Spaceship;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

    private final SpaceshipService spaceshipService;

    @GetMapping
    public List<SpaceshipDTO> getAllSpaceships() {
        return spaceshipService.getAllSpaceships();
    }

    @GetMapping("/paginated")
    public Page<SpaceshipDTO> getAllSpaceshipsPaginated(Pageable pageable) {
        return spaceshipService.getAllSpaceshipsPaginated(pageable);
    }

    @GetMapping("/{id}")
    public SpaceshipDTO getById(@PathVariable Long id) {
        return spaceshipService.getById(id);
    }

    @PostMapping("/search")
    public List<SpaceshipDTO> getSpaceshipsByFilter(@RequestBody SpaceshipFilter filter) {
        return spaceshipService.getSpaceshipsByFilter(filter);
    }

    @PostMapping("/create")
    public SpaceshipDTO create(@RequestBody SpaceshipDTO dto) {
        return spaceshipService.create(dto);
    }
}
