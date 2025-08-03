package com.example.app;

/**
 * Default implementation of RequestSpec
 */
public class DefaultRequestSpec implements RequestBodySpec {
    private final HttpMethod method;
    private final String baseUrl;
    private final HttpHeaders headers;
    private String uri = "";
    private Object requestBody;
    
    public DefaultRequestSpec(HttpMethod method, String baseUrl, HttpHeaders defaultHeaders) {
        this.method = method;
        this.baseUrl = baseUrl;
        this.headers = new HttpHeaders();
        
        // Copy default headers
        for (String name : defaultHeaders.keySet()) {
            for (String value : defaultHeaders.get(name)) {
                this.headers.add(name, value);
            }
        }
    }
    
    public DefaultRequestSpec uri(String uri) {
        this.uri = uri;
        return this;
    }
    
    public DefaultRequestSpec uri(String uri, Object... uriVariables) {
        // Simple URI template processing (basic implementation)
        String processedUri = uri;
        for (int i = 0; i < uriVariables.length; i++) {
            processedUri = processedUri.replace("{" + i + "}", uriVariables[i].toString());
        }
        this.uri = processedUri;
        return this;
    }
    
    @Override
    public DefaultRequestSpec header(String name, String... values) {
        for (String value : values) {
            headers.add(name, value);
        }
        return this;
    }
    
    @Override
    public DefaultRequestSpec headers(HttpHeaders headers) {
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                this.headers.add(name, value);
            }
        }
        return this;
    }
    
    @Override
    public RequestBodySpec body(Object body) {
        this.requestBody = body;
        return this;
    }
    
    // Terminal operations
    public ResponseSpec retrieve() {
        return new ResponseSpecImpl(this);
    }
    
    public <T> ResponseEntity<T> exchange(Class<T> responseType) {
        return executeRequest(responseType);
    }
    
    // Simulate HTTP request execution
    private <T> ResponseEntity<T> executeRequest(Class<T> responseType) {
        String fullUrl = baseUrl + uri;
        
        System.out.println("Executing " + method + " request to: " + fullUrl);
        System.out.println("Headers: " + headers.keySet());
        if (requestBody != null) {
            System.out.println("Request body: " + requestBody);
        }
        
        // Simulate response
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");
        
        T responseBody = null;
        if (responseType == String.class) {
            responseBody = responseType.cast("{\"message\": \"Success\", \"method\": \"" + method + "\"}");
        }
        
        return new ResponseEntity<>(responseBody, responseHeaders, 200);
    }
    
    // Response specification implementation
    private static class ResponseSpecImpl implements ResponseSpec {
        private final DefaultRequestSpec requestSpec;
        
        public ResponseSpecImpl(DefaultRequestSpec requestSpec) {
            this.requestSpec = requestSpec;
        }
        
        @Override
        public <T> T body(Class<T> bodyType) {
            ResponseEntity<T> response = requestSpec.executeRequest(bodyType);
            return response.getBody();
        }
        
        @Override
        public <T> ResponseEntity<T> toEntity(Class<T> bodyType) {
            return requestSpec.executeRequest(bodyType);
        }
        
        @Override
        public ResponseEntity<Void> toBodilessEntity() {
            return requestSpec.executeRequest(Void.class);
        }
    }
}
