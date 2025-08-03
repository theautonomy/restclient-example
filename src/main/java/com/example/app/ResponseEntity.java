package com.example.app;

/**
 * ResponseEntity to wrap response data
 */
public class ResponseEntity<T> {
    private final T body;
    private final HttpHeaders headers;
    private final int statusCode;
    
    public ResponseEntity(T body, HttpHeaders headers, int statusCode) {
        this.body = body;
        this.headers = headers;
        this.statusCode = statusCode;
    }
    
    public T getBody() { return body; }
    public HttpHeaders getHeaders() { return headers; }
    public int getStatusCode() { return statusCode; }
    public boolean is2xxSuccessful() { return statusCode >= 200 && statusCode < 300; }
}
