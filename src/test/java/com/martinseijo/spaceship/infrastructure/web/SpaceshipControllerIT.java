package com.martinseijo.spaceship.infrastructure.web;

import com.martinseijo.spaceship.domain.model.Spaceship;
import com.martinseijo.spaceship.domain.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpaceshipControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpaceshipRepository repository;

    private Long spaceshipId;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        Spaceship spaceship = repository.save(new Spaceship(null, "Enterprise"));
        spaceshipId = spaceship.getId();
    }

    @Test
    void testGetAllSpaceships() throws Exception {
        mockMvc.perform(get("/spaceships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Enterprise"));
    }

    @Test
    void testGetSpaceshipById() throws Exception {
        mockMvc.perform(get("/spaceships/" + spaceshipId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enterprise"));
    }

    @Test
    void testCreateSpaceship() throws Exception {
        String newSpaceship = """
                {
                    "name": "Millennium Falcon"
                }
                """;

        mockMvc.perform(post("/spaceships/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newSpaceship))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Millennium Falcon"));
    }

    @Test
    void testUpdateSpaceship() throws Exception {
        String updatedSpaceship = """
                {
                    "id": %d,
                    "name": "Updated Enterprise"
                }
                """.formatted(spaceshipId);

        mockMvc.perform(put("/spaceships/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSpaceship))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Enterprise"));
    }

    @Test
    void testDeleteSpaceship() throws Exception {
        mockMvc.perform(delete("/spaceships/delete/" + spaceshipId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/spaceships/" + spaceshipId))
                .andExpect(status().isNotFound());
    }
}
