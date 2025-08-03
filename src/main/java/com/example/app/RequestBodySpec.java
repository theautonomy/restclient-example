package com.example.app;

/**
 * Interface for request body specification.
 * Extends RequestHeadersSpec to provide body submission capabilities
 * while maintaining the header and URI configuration methods.
 */
public interface RequestBodySpec extends RequestHeadersSpec<RequestBodySpec> {
    /**
     * Sets the body of the request
     * @param body The body to send with the request
     * @return this RequestBodySpec for method chaining
     */
    RequestBodySpec body(Object body);
}
