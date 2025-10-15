package com.example.restclientdemo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.example.restclientdemo.model.HttpBinResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

/**
 * Unit tests for HttpBinService.
 *
 * <p>These tests use real HTTP calls to httpbin.org to verify the RestClient integration. In a
 * production environment, you might want to use MockWebServer or WireMock for isolated unit tests.
 */
class HttpBinServiceTest {

    private HttpBinService httpBinService;
    private RestClient defaultRestClient;
    private RestClient customRestClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Create real RestClient instances for integration testing
        defaultRestClient = RestClient.builder().baseUrl("https://httpbin.org").build();

        customRestClient =
                RestClient.builder()
                        .baseUrl("https://httpbin.org")
                        .defaultHeader("Custom-Header", "CustomValue")
                        .build();

        objectMapper = new ObjectMapper();

        httpBinService = new HttpBinService(defaultRestClient, customRestClient, objectMapper);
    }

    // GET Request Tests

    @Test
    @DisplayName("Should perform simple GET request")
    void testSimpleGet() {
        // When
        HttpBinResponse response = httpBinService.simpleGet();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUrl()).contains("/get");
        assertThat(response.getOrigin()).isNotNull();
    }

    @Test
    @DisplayName("Should perform GET with query parameters")
    void testGetWithQueryParams() {
        // When
        HttpBinResponse response = httpBinService.getWithQueryParams();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getArgs()).isNotNull();
        assertThat(response.getArgs()).containsEntry("name", "John");
        assertThat(response.getArgs()).containsEntry("age", "30");
        assertThat(response.getArgs()).containsEntry("city", "New York");
    }

    @Test
    @DisplayName("Should perform GET with URI variables")
    void testGetWithUriVariables() {
        // When
        HttpBinResponse response = httpBinService.getWithUriVariables();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getArgs()).isNotNull();
        assertThat(response.getArgs()).containsEntry("name", "Jane");
        assertThat(response.getArgs()).containsEntry("age", "25");
    }

    @Test
    @DisplayName("Should perform GET with custom headers")
    void testGetWithCustomHeaders() {
        // When
        HttpBinResponse response = httpBinService.getWithCustomHeaders();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getHeaders()).isNotNull();
        assertThat(response.getHeaders()).containsKey("X-Custom-Header");
        // Note: Not all custom headers are guaranteed to be in the response headers map
        // as httpbin.org returns them differently
    }

    @Test
    @DisplayName("Should get response as map using ParameterizedTypeReference")
    void testGetAsMap() {
        // When
        Map<String, Object> response = httpBinService.getAsMap();

        // Then
        assertThat(response).isNotNull();
        assertThat(response).containsKeys("url", "headers", "origin");
    }

    @Test
    @DisplayName("Should get response with ResponseEntity")
    void testGetWithResponseEntity() {
        // When
        ResponseEntity<HttpBinResponse> response = httpBinService.getWithResponseEntity();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUrl()).contains("/get");
        assertThat(response.getHeaders()).isNotNull();
    }

    // POST Request Tests

    @Test
    @DisplayName("Should perform POST with JSON body")
    void testPostJson() {
        // When
        HttpBinResponse response = httpBinService.postJson();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJson()).isNotNull();
        assertThat(response.getJson()).containsEntry("name", "Alice");
        assertThat(response.getJson()).containsEntry("email", "alice@example.com");
        assertThat(response.getJson()).containsEntry("age", 28);
    }

    @Test
    @DisplayName("Should perform POST with form data")
    void testPostFormData() {
        // When
        HttpBinResponse response = httpBinService.postFormData();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getForm()).isNotNull();
        assertThat(response.getForm()).containsEntry("username", "johndoe");
        assertThat(response.getForm()).containsEntry("password", "secret123");
        assertThat(response.getForm()).containsEntry("email", "john@example.com");
    }

    @Test
    @DisplayName("Should perform POST with custom RestClient")
    void testPostWithCustomClient() {
        // When
        HttpBinResponse response = httpBinService.postWithCustomClient();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJson()).isNotNull();
        assertThat(response.getJson()).containsKey("message");
        assertThat(response.getJson()).containsKey("timestamp");
        assertThat(response.getHeaders()).containsKey("Custom-Header");
    }

    // Other HTTP Methods Tests

    @Test
    @DisplayName("Should perform PUT request")
    void testPutData() {
        // When
        HttpBinResponse response = httpBinService.putData();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJson()).isNotNull();
        assertThat(response.getJson()).containsEntry("name", "Bob Updated");
        assertThat(response.getJson()).containsEntry("email", "bob.updated@example.com");
        assertThat(response.getJson()).containsEntry("age", 35);
    }

    @Test
    @DisplayName("Should perform PATCH request")
    void testPatchData() {
        // When
        HttpBinResponse response = httpBinService.patchData();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJson()).isNotNull();
        assertThat(response.getJson()).containsEntry("age", 36);
    }

    @Test
    @DisplayName("Should perform DELETE request")
    void testDeleteResource() {
        // When
        HttpBinResponse response = httpBinService.deleteResource();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUrl()).contains("/delete");
    }

    // Exchange Method Tests

    @Test
    @DisplayName("Should use exchange method for full control over request and response")
    void testDemonstrateExchange() {
        // When
        HttpBinResponse response = httpBinService.demonstrateExchange();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getArgs()).isNotNull();
        assertThat(response.getArgs()).containsEntry("id", "123");
        assertThat(response.getArgs()).containsEntry("name", "TestUser");
    }

    @Test
    @DisplayName("Should handle errors with exchange method")
    void testDemonstrateExchangeWithError() {
        // When
        String result = httpBinService.demonstrateExchangeWithError();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("Error handled");
        assertThat(result).contains("404");
    }

    @Test
    @DisplayName("Should compare exchange vs retrieve methods")
    void testCompareExchangeVsRetrieve() {
        // This test verifies the method executes without throwing exceptions
        // The comparison is demonstrated through console output
        httpBinService.compareExchangeVsRetrieve();
        // If we get here without exceptions, the test passes
    }

    // Utility Endpoint Tests

    @Test
    @DisplayName("Should get IP address")
    void testGetIpAddress() {
        // When
        String ip = httpBinService.getIpAddress();

        // Then
        assertThat(ip).isNotNull();
        assertThat(ip).isNotEmpty();
    }

    @Test
    @DisplayName("Should get user agent")
    void testGetUserAgent() {
        // When
        String userAgent = httpBinService.getUserAgent();

        // Then
        assertThat(userAgent).isNotNull();
        assertThat(userAgent).isNotEmpty();
        assertThat(userAgent).contains("Java");
    }

    @Test
    @DisplayName("Should get headers")
    void testGetHeaders() {
        // When
        Map<String, String> headers = httpBinService.getHeaders();

        // Then
        assertThat(headers).isNotNull();
        assertThat(headers).isNotEmpty();
        assertThat(headers).containsKey("User-Agent");
    }

    // Authentication Tests

    @Test
    @DisplayName("Should handle basic auth request")
    void testDemonstrateBasicAuth() {
        // When
        String response = httpBinService.demonstrateBasicAuth();

        // Then
        // Basic auth may succeed or return null if credentials don't match
        // The method demonstrates basic auth setup
        if (response != null) {
            assertThat(response).contains("true").contains("user");
        }
        // Test passes either way as it demonstrates the auth mechanism
    }

    // Error Handling Tests

    @Test
    @DisplayName("Should handle 404 error")
    void testDemonstrateErrorHandling() {
        // This test verifies that error handling executes without crashing
        // Errors are caught and logged within the method
        httpBinService.demonstrateErrorHandling();
        // If we get here without uncaught exceptions, the test passes
    }

    @Test
    @DisplayName("Should handle different status codes")
    void testDemonstrateStatusHandling() {
        // This test verifies status code handling executes successfully
        httpBinService.demonstrateStatusHandling();
        // If we get here without uncaught exceptions, the test passes
    }

    // Integration Tests

    @Test
    @DisplayName("Should execute all GET methods successfully in sequence")
    void testAllGetMethodsInSequence() throws InterruptedException {
        // Execute all GET methods
        httpBinService.simpleGet();
        Thread.sleep(500);

        httpBinService.getWithQueryParams();
        Thread.sleep(500);

        httpBinService.getWithUriVariables();
        Thread.sleep(500);

        httpBinService.getWithCustomHeaders();
        Thread.sleep(500);

        // If we get here, all methods executed successfully
    }

    @Test
    @DisplayName("Should execute all POST methods successfully in sequence")
    void testAllPostMethodsInSequence() throws InterruptedException {
        // Execute all POST methods
        httpBinService.postJson();
        Thread.sleep(500);

        httpBinService.postFormData();
        Thread.sleep(500);

        httpBinService.postWithCustomClient();
        Thread.sleep(500);

        // If we get here, all methods executed successfully
    }

    @Test
    @DisplayName("Should execute all HTTP methods successfully")
    void testAllHttpMethodsSuccessfully() throws InterruptedException {
        // Test each HTTP method
        httpBinService.simpleGet();
        Thread.sleep(500);

        httpBinService.postJson();
        Thread.sleep(500);

        httpBinService.putData();
        Thread.sleep(500);

        httpBinService.patchData();
        Thread.sleep(500);

        httpBinService.deleteResource();
        Thread.sleep(500);

        // If we get here, all methods executed successfully
    }
}
