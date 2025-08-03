package com.example.app;

/**
 * Implementation of RestClient interface
 */
public class RestClientImpl implements RestClient {
    private final String baseUrl;
    private final HttpHeaders defaultHeaders;
    
    private RestClientImpl(String baseUrl, HttpHeaders defaultHeaders) {
        this.baseUrl = baseUrl != null ? baseUrl : "";
        this.defaultHeaders = defaultHeaders != null ? defaultHeaders : new HttpHeaders();
    }
    
    @Override
    public RequestHeadersSpec<?> get() {
        return new DefaultRequestSpec(HttpMethod.GET, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestBodySpec post() {
        return new DefaultRequestSpec(HttpMethod.POST, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestBodySpec put() {
        return new DefaultRequestSpec(HttpMethod.PUT, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestBodySpec patch() {
        return new DefaultRequestSpec(HttpMethod.PATCH, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestBodySpec delete() {
        return new DefaultRequestSpec(HttpMethod.DELETE, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestHeadersSpec<?> head() {
        return new DefaultRequestSpec(HttpMethod.HEAD, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestHeadersSpec<?> options() {
        return new DefaultRequestSpec(HttpMethod.OPTIONS, baseUrl, defaultHeaders);
    }
    
    @Override
    public RequestHeadersSpec<?> method(HttpMethod method) {
        return new DefaultRequestSpec(method, baseUrl, defaultHeaders);
    }
    
    // Builder implementation
    public static class BuilderImpl implements RestClient.Builder {
        private String baseUrl;
        private HttpHeaders defaultHeaders = new HttpHeaders();
        
        @Override
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        
        @Override
        public Builder defaultHeader(String name, String... values) {
            for (String value : values) {
                defaultHeaders.add(name, value);
            }
            return this;
        }
        
        @Override
        public Builder defaultHeaders(HttpHeaders headers) {
            for (String name : headers.keySet()) {
                for (String value : headers.get(name)) {
                    defaultHeaders.add(name, value);
                }
            }
            return this;
        }
        
        @Override
        public RestClient build() {
            return new RestClientImpl(baseUrl, defaultHeaders);
        }
    }
}
