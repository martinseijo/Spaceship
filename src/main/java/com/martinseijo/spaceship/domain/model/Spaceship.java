package com.martinseijo.spaceship.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spaceship")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
