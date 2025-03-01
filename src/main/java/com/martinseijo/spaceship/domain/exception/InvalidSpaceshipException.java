package com.martinseijo.spaceship.domain.exception;

public class InvalidSpaceshipException extends RuntimeException {
    public InvalidSpaceshipException(String message) {
        super(message);
    }
}
