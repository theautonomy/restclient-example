package com.example.app;

/**
 * Interface for request headers specification
 */
public interface RequestHeadersSpec<S extends RequestHeadersSpec<S>> {
    S header(String name, String... values);
    S headers(HttpHeaders headers);
    S uri(String uri);
    S uri(String uri, Object... uriVariables);
    ResponseSpec retrieve();
    <T> ResponseEntity<T> exchange(Class<T> responseType);
}
