package com.martinseijo.spaceship.infrastructure.web.impl;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import com.martinseijo.spaceship.domain.service.SpaceshipService;
import com.martinseijo.spaceship.infrastructure.web.SpaceshipController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
@Tag(name = "Spaceships", description = "API for managing spaceships")
public class SpaceshipControllerImpl implements SpaceshipController {

    private final SpaceshipService spaceshipService;

    @Override
    @Operation(summary = "Get all spaceships", description = "Retrieve a list of all spaceships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceships",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceships not found",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<List<SpaceshipDTO>> getAllSpaceships() {
        List<SpaceshipDTO> spaceships = spaceshipService.getAllSpaceships();
        if (spaceships.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spaceships);
    }

    @Override
    @Operation(summary = "Get paginated spaceships", description = "Retrieve a paginated list of spaceships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceships",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceships not found",
                    content = @Content) })
    @GetMapping("/paginated")
    public ResponseEntity<Page<SpaceshipDTO>> getAllSpaceshipsPaginated(Pageable pageable) {
        Page<SpaceshipDTO> result = spaceshipService.getAllSpaceshipsPaginated(pageable);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Override
    @Operation(summary = "Get spaceship by ID", description = "Retrieve a spaceship by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceship",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceship not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<SpaceshipDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        SpaceshipDTO spaceship = spaceshipService.getById(id);
        return ResponseEntity.ok(spaceship);
    }

    @Override
    @Operation(summary = "Search spaceships by filter", description = "Search for spaceships using a filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceships",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceships not found",
                    content = @Content) })
    @PostMapping("/search")
    public ResponseEntity<List<SpaceshipDTO>> getSpaceshipsByFilter(@RequestBody SpaceshipFilter filter) {
        List<SpaceshipDTO> spaceships = spaceshipService.getSpaceshipsByFilter(filter);
        if (spaceships.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spaceships);
    }

    @Override
    @Operation(summary = "Create a new spaceship", description = "Create a new spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Spaceship created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content) })
    @PostMapping("/create")
    public ResponseEntity<SpaceshipDTO> create(@RequestBody SpaceshipDTO dto) {
        SpaceshipDTO createdSpaceship = spaceshipService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpaceship);
    }

    @Override
    @Operation(summary = "Update a spaceship", description = "Update an existing spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceship not found",
                    content = @Content) })
    @PutMapping("/update")
    public ResponseEntity<SpaceshipDTO> update(@RequestBody SpaceshipDTO dto) throws ResourceNotFoundException {
        SpaceshipDTO updatedSpaceship = spaceshipService.update(dto);
        return ResponseEntity.ok(updatedSpaceship);
    }

    @Override
    @Operation(summary = "Delete a spaceship by ID", description = "Delete a spaceship by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceship not found",
                    content = @Content) })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        spaceshipService.delete(id);
        return ResponseEntity.ok().build();
    }
}