package com.example.app;

/**
 * Main RestClient interface and its specifications
 */
public interface RestClient {
    
    // HTTP method specifications
    RequestHeadersSpec<?> get();
    RequestBodySpec post();
    RequestBodySpec put();
    RequestBodySpec patch();
    RequestHeadersSpec<?> delete();
    RequestHeadersSpec<?> head();
    RequestHeadersSpec<?> options();
    
    // Method with URI
    RequestHeadersSpec<?> method(HttpMethod method);
    
    // Builder interface
    interface Builder {
        Builder baseUrl(String baseUrl);
        Builder defaultHeader(String name, String... values);
        Builder defaultHeaders(HttpHeaders headers);
        RestClient build();
    }
    
    static Builder builder() {
        return new RestClientImpl.BuilderImpl();
    }
    
    static RestClient create() {
        return builder().build();
    }
    
    static RestClient create(String baseUrl) {
        return builder().baseUrl(baseUrl).build();
    }
}
