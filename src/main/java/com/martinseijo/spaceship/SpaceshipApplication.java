package com.martinseijo.spaceship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpaceshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceshipApplication.class, args);
	}

}
