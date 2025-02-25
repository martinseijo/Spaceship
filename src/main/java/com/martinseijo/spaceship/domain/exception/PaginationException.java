package com.martinseijo.spaceship.domain.exception;

public class PaginationException extends RuntimeException {
  public PaginationException(String message, Throwable cause) {
    super(message, cause);
  }
}
