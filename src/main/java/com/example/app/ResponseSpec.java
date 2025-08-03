package com.example.app;

/**
 * Interface for response specification
 */
public interface ResponseSpec {
    <T> T body(Class<T> bodyType);
    <T> ResponseEntity<T> toEntity(Class<T> bodyType);
    ResponseEntity<Void> toBodilessEntity();
}
