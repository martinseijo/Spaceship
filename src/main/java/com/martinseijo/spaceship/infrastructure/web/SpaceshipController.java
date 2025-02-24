package com.martinseijo.spaceship.infrastructure.web;

import com.martinseijo.spaceship.application.dto.SpaceshipDTO;
import com.martinseijo.spaceship.application.dto.SpaceshipFilter;
import com.martinseijo.spaceship.application.service.SpaceshipService;
import com.martinseijo.spaceship.domain.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
@Tag(name = "Spaceships", description = "API for managing spaceships")
public class SpaceshipController {

    private final SpaceshipService spaceshipService;

    @Operation(summary = "Get all spaceships", description = "Retrieve a list of all spaceships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceships",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceships not found",
                    content = @Content) })
    @GetMapping
    public List<SpaceshipDTO> getAllSpaceships() {
        return spaceshipService.getAllSpaceships();
    }

    @Operation(summary = "Get paginated spaceships", description = "Retrieve a paginated list of spaceships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceships",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceships not found",
                    content = @Content) })
    @GetMapping("/paginated")
    public Page<SpaceshipDTO> getAllSpaceshipsPaginated(Pageable pageable) {
        return spaceshipService.getAllSpaceshipsPaginated(pageable);
    }

    @Operation(summary = "Get spaceship by ID", description = "Retrieve a spaceship by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceship",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceship not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public SpaceshipDTO getById(@PathVariable Long id) throws ResourceNotFoundException {
        return spaceshipService.getById(id);
    }

    @Operation(summary = "Search spaceships by filter", description = "Search for spaceships using a filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the spaceships",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceships not found",
                    content = @Content) })
    @PostMapping("/search")
    public List<SpaceshipDTO> getSpaceshipsByFilter(@RequestBody SpaceshipFilter filter) {
        return spaceshipService.getSpaceshipsByFilter(filter);
    }

    @Operation(summary = "Create a new spaceship", description = "Create a new spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Spaceship created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content) })
    @PostMapping("/create")
    public SpaceshipDTO create(@RequestBody SpaceshipDTO dto) {
        return spaceshipService.create(dto);
    }

    @Operation(summary = "Update a spaceship", description = "Update an existing spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceship not found",
                    content = @Content) })
    @PutMapping("/update")
    public SpaceshipDTO update(@RequestBody SpaceshipDTO dto) throws ResourceNotFoundException {
        return spaceshipService.update(dto);
    }

    @Operation(summary = "Delete a spaceship by ID", description = "Delete a spaceship by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceshipDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Spaceship not found",
                    content = @Content) })
    @DeleteMapping("/delete/{id}")
    public SpaceshipDTO deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        return spaceshipService.delete(id);
    }
}