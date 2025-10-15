package com.example.restclientdemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.restclientdemo.exception.ClientErrorException;
import com.example.restclientdemo.exception.ServerErrorException;
import com.example.restclientdemo.handler.CustomResponseErrorHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

/**
 * Unit tests for ErrorHandlingService.
 *
 * <p>These tests verify the error handling functionality with RestClient using custom error
 * handlers.
 */
class ErrorHandlingServiceTest {

    private ErrorHandlingService errorHandlingService;
    private RestClient errorHandlingRestClient;
    private RestClient defaultRestClient;

    @BeforeEach
    void setUp() {
        // Create RestClient with custom error handler
        errorHandlingRestClient =
                RestClient.builder()
                        .baseUrl("https://httpbin.org")
                        .defaultStatusHandler(new CustomResponseErrorHandler())
                        .build();

        // Create default RestClient without custom error handler
        defaultRestClient = RestClient.builder().baseUrl("https://httpbin.org").build();

        // Create service
        errorHandlingService = new ErrorHandlingService(errorHandlingRestClient, defaultRestClient);
    }

    @Nested
    @DisplayName("Client Error Handling Tests (4xx)")
    class ClientErrorTests {

        @Test
        @DisplayName("Should throw ClientErrorException for 404 Not Found")
        void testHandle404NotFound() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/404")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class)
                    .hasMessageContaining("404")
                    .satisfies(
                            ex -> {
                                ClientErrorException clientEx = (ClientErrorException) ex;
                                assertThat(clientEx.getStatusCode()).isEqualTo(404);
                                assertThat(clientEx.getResponseBody()).isNotNull();
                            });
        }

        @Test
        @DisplayName("Should throw ClientErrorException for 401 Unauthorized")
        void testHandle401Unauthorized() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/401")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class)
                    .hasMessageContaining("401")
                    .satisfies(
                            ex -> {
                                ClientErrorException clientEx = (ClientErrorException) ex;
                                assertThat(clientEx.getStatusCode()).isEqualTo(401);
                            });
        }

        @Test
        @DisplayName("Should throw ClientErrorException for 400 Bad Request")
        void testHandle400BadRequest() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/400")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class)
                    .satisfies(
                            ex -> {
                                ClientErrorException clientEx = (ClientErrorException) ex;
                                assertThat(clientEx.getStatusCode()).isEqualTo(400);
                            });
        }

        @Test
        @DisplayName("Should throw ClientErrorException for 403 Forbidden")
        void testHandle403Forbidden() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/403")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class)
                    .satisfies(
                            ex -> {
                                ClientErrorException clientEx = (ClientErrorException) ex;
                                assertThat(clientEx.getStatusCode()).isEqualTo(403);
                            });
        }
    }

    @Nested
    @DisplayName("Server Error Handling Tests (5xx)")
    class ServerErrorTests {

        @Test
        @DisplayName("Should throw ServerErrorException for 500 Internal Server Error")
        void testHandle500InternalServerError() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/500")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ServerErrorException.class)
                    .hasMessageContaining("500")
                    .satisfies(
                            ex -> {
                                ServerErrorException serverEx = (ServerErrorException) ex;
                                assertThat(serverEx.getStatusCode()).isEqualTo(500);
                                assertThat(serverEx.getResponseBody()).isNotNull();
                            });
        }

        @Test
        @DisplayName("Should throw ServerErrorException for 503 Service Unavailable")
        void testHandle503ServiceUnavailable() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/503")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ServerErrorException.class)
                    .hasMessageContaining("503")
                    .satisfies(
                            ex -> {
                                ServerErrorException serverEx = (ServerErrorException) ex;
                                assertThat(serverEx.getStatusCode()).isEqualTo(503);
                            });
        }

        @Test
        @DisplayName("Should throw ServerErrorException for 502 Bad Gateway")
        void testHandle502BadGateway() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/502")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ServerErrorException.class)
                    .satisfies(
                            ex -> {
                                ServerErrorException serverEx = (ServerErrorException) ex;
                                assertThat(serverEx.getStatusCode()).isEqualTo(502);
                            });
        }
    }

    @Nested
    @DisplayName("Error Handling Demonstration Tests")
    class DemonstrationTests {

        @Test
        @DisplayName("Should execute error handling demonstration without crashing")
        void testDemonstrateErrorHandling() {
            // This test verifies that the demonstration handles errors gracefully
            // and doesn't throw uncaught exceptions
            errorHandlingService.demonstrateErrorHandling();
            // If we get here, all errors were handled gracefully
        }

        @Test
        @DisplayName("Should execute error recovery demonstration")
        void testDemonstrateErrorRecovery() {
            // This test verifies error recovery strategies execute properly
            errorHandlingService.demonstrateErrorRecovery();
            // If we get here, error recovery mechanisms worked
        }
    }

    @Nested
    @DisplayName("Inline Error Handling Tests")
    class InlineErrorHandlingTests {

        @Test
        @DisplayName("Should handle error with inline status handler")
        void testInlineErrorHandling() {
            // When/Then
            assertThatThrownBy(
                            () ->
                                    defaultRestClient
                                            .get()
                                            .uri("/status/400")
                                            .retrieve()
                                            .onStatus(
                                                    status -> status.is4xxClientError(),
                                                    (request, response) -> {
                                                        throw new RuntimeException(
                                                                "Custom inline error: "
                                                                        + response.getStatusCode());
                                                    })
                                            .body(String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Custom inline error");
        }

        @Test
        @DisplayName("Should handle multiple status codes with inline handlers")
        void testMultipleInlineHandlers() {
            // Test 4xx handler
            assertThatThrownBy(
                            () ->
                                    defaultRestClient
                                            .get()
                                            .uri("/status/404")
                                            .retrieve()
                                            .onStatus(
                                                    status -> status.is4xxClientError(),
                                                    (request, response) -> {
                                                        throw new RuntimeException("4xx error");
                                                    })
                                            .onStatus(
                                                    status -> status.is5xxServerError(),
                                                    (request, response) -> {
                                                        throw new RuntimeException("5xx error");
                                                    })
                                            .body(String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("4xx error");

            // Test 5xx handler
            assertThatThrownBy(
                            () ->
                                    defaultRestClient
                                            .get()
                                            .uri("/status/500")
                                            .retrieve()
                                            .onStatus(
                                                    status -> status.is4xxClientError(),
                                                    (request, response) -> {
                                                        throw new RuntimeException("4xx error");
                                                    })
                                            .onStatus(
                                                    status -> status.is5xxServerError(),
                                                    (request, response) -> {
                                                        throw new RuntimeException("5xx error");
                                                    })
                                            .body(String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("5xx error");
        }
    }

    @Nested
    @DisplayName("Error Recovery Strategy Tests")
    class ErrorRecoveryTests {

        @Test
        @DisplayName("Should return fallback value on error")
        void testFallbackValue() {
            // When
            String result;
            try {
                errorHandlingRestClient.get().uri("/status/500").retrieve().body(String.class);
                result = "Success";
            } catch (ServerErrorException e) {
                result = "Fallback value";
            }

            // Then
            assertThat(result).isEqualTo("Fallback value");
        }

        @Test
        @DisplayName("Should retry on error")
        void testRetryLogic() {
            // Simulate retry logic
            int maxRetries = 3;
            int attempts = 0;
            String result = null;

            while (attempts < maxRetries) {
                try {
                    attempts++;
                    result =
                            errorHandlingRestClient
                                    .get()
                                    .uri("/status/500")
                                    .retrieve()
                                    .body(String.class);
                    break; // Success
                } catch (ServerErrorException e) {
                    if (attempts >= maxRetries) {
                        result = "Fallback after retries";
                    }
                }
            }

            // Then
            assertThat(attempts).isEqualTo(maxRetries);
            assertThat(result).isEqualTo("Fallback after retries");
        }

        @Test
        @DisplayName("Should handle successful response without error")
        void testSuccessfulResponseWithoutError() {
            // When
            String result =
                    defaultRestClient.get().uri("/status/200").retrieve().body(String.class);

            // Then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Exception Model Tests")
    class ExceptionModelTests {

        @Test
        @DisplayName("Should create ClientErrorException with all properties")
        void testClientErrorException() {
            // Given
            String message = "Client error occurred";
            int statusCode = 404;
            String responseBody = "Not Found";

            // When
            ClientErrorException exception =
                    new ClientErrorException(message, statusCode, responseBody);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getStatusCode()).isEqualTo(statusCode);
            assertThat(exception.getResponseBody()).isEqualTo(responseBody);
            assertThat(exception.toString()).contains("404");
            assertThat(exception.toString()).contains("Client error occurred");
        }

        @Test
        @DisplayName("Should create ServerErrorException with all properties")
        void testServerErrorException() {
            // Given
            String message = "Server error occurred";
            int statusCode = 500;
            String responseBody = "Internal Server Error";

            // When
            ServerErrorException exception =
                    new ServerErrorException(message, statusCode, responseBody);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getStatusCode()).isEqualTo(statusCode);
            assertThat(exception.getResponseBody()).isEqualTo(responseBody);
            assertThat(exception.toString()).contains("500");
            assertThat(exception.toString()).contains("Server error occurred");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle multiple error types in sequence")
        void testMultipleErrorTypesInSequence() {
            // Test 404
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/404")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class);

            // Test 401
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/401")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class);

            // Test 500
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/500")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ServerErrorException.class);

            // Test 503
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/503")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ServerErrorException.class);
        }

        @Test
        @DisplayName("Should handle mixed success and error responses")
        void testMixedSuccessAndErrorResponses() throws InterruptedException {
            // Success
            String successResult =
                    defaultRestClient.get().uri("/status/200").retrieve().body(String.class);
            assertThat(successResult).isNotNull();
            Thread.sleep(500);

            // Error
            assertThatThrownBy(
                            () ->
                                    errorHandlingRestClient
                                            .get()
                                            .uri("/status/404")
                                            .retrieve()
                                            .body(String.class))
                    .isInstanceOf(ClientErrorException.class);
            Thread.sleep(500);

            // Success again
            String successResult2 =
                    defaultRestClient.get().uri("/get").retrieve().body(String.class);
            assertThat(successResult2).isNotNull();
        }
    }
}
