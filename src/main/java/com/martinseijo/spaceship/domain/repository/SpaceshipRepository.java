package com.martinseijo.spaceship.domain.repository;

import com.martinseijo.spaceship.domain.model.Spaceship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {

    Page<Spaceship> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
