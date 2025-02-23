package com.martinseijo.spaceship.domain.repository;

import com.martinseijo.spaceship.domain.model.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {

    List<Spaceship> findByNameContainingIgnoreCase(String name);
}
