package com.martinseijo.spaceship.domain.repository;

import com.martinseijo.spaceship.domain.model.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
}
