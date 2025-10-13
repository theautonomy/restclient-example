package com.example.restclientdemo.service;

import com.example.restclientdemo.exception.ClientErrorException;
import com.example.restclientdemo.exception.ServerErrorException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/** Service demonstrating various error handling strategies with RestClient. */
@Service
public class ErrorHandlingService {

    private final RestClient errorHandlingRestClient;
    private final RestClient defaultRestClient;

    public ErrorHandlingService(
            @Qualifier("errorHandlingRestClient") RestClient errorHandlingRestClient,
            @Qualifier("defaultRestClient") RestClient defaultRestClient) {
        this.errorHandlingRestClient = errorHandlingRestClient;
        this.defaultRestClient = defaultRestClient;
    }

    public void demonstrateErrorHandling() {
        System.out.println("\n=== Error Handling Demo ===");

        // Test 404 Not Found (Client Error)
        handle404NotFound();

        // Test 500 Internal Server Error (Server Error)
        handle500InternalServerError();

        // Test 401 Unauthorized (Client Error)
        handle401Unauthorized();

        // Test 503 Service Unavailable (Server Error)
        handle503ServiceUnavailable();

        // Demonstrate inline error handling with defaultStatusHandler
        demonstrateInlineErrorHandling();
    }

    /**
     * Handles 404 Not Found error. Demonstrates catching ClientErrorException for 4xx status codes.
     */
    private void handle404NotFound() {
        System.out.println("\n1. Testing 404 Not Found:");
        try {
            String response =
                    errorHandlingRestClient.get().uri("/status/404").retrieve().body(String.class);
            System.out.println("Response: " + response);
        } catch (ClientErrorException e) {
            System.out.println("Caught ClientErrorException:");
            System.out.println("  Status Code: " + e.getStatusCode());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Response Body: " + e.getResponseBody());
            System.out.println("  Error handled gracefully!");
        }
    }

    /**
     * Handles 500 Internal Server Error. Demonstrates catching ServerErrorException for 5xx status
     * codes.
     */
    private void handle500InternalServerError() {
        System.out.println("\n2. Testing 500 Internal Server Error:");
        try {
            String response =
                    errorHandlingRestClient.get().uri("/status/500").retrieve().body(String.class);
            System.out.println("Response: " + response);
        } catch (ServerErrorException e) {
            System.out.println("Caught ServerErrorException:");
            System.out.println("  Status Code: " + e.getStatusCode());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Response Body: " + e.getResponseBody());
            System.out.println("  Error handled gracefully!");
        }
    }

    /** Handles 401 Unauthorized error. Demonstrates handling authentication errors. */
    private void handle401Unauthorized() {
        System.out.println("\n3. Testing 401 Unauthorized:");
        try {
            String response =
                    errorHandlingRestClient.get().uri("/status/401").retrieve().body(String.class);
            System.out.println("Response: " + response);
        } catch (ClientErrorException e) {
            System.out.println("Caught ClientErrorException (Unauthorized):");
            System.out.println("  Status Code: " + e.getStatusCode());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Action: Could trigger authentication flow here");
        }
    }

    /**
     * Handles 503 Service Unavailable error. Demonstrates handling temporary service disruptions.
     */
    private void handle503ServiceUnavailable() {
        System.out.println("\n4. Testing 503 Service Unavailable:");
        try {
            String response =
                    errorHandlingRestClient.get().uri("/status/503").retrieve().body(String.class);
            System.out.println("Response: " + response);
        } catch (ServerErrorException e) {
            System.out.println("Caught ServerErrorException (Service Unavailable):");
            System.out.println("  Status Code: " + e.getStatusCode());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Action: Could implement retry logic here");
        }
    }

    /**
     * Demonstrates inline error handling using defaultStatusHandler. This approach allows
     * per-request error handling without a global handler.
     */
    private void demonstrateInlineErrorHandling() {
        System.out.println("\n5. Demonstrating inline error handling:");

        try {
            String response =
                    defaultRestClient
                            .get()
                            .uri("/status/400")
                            .retrieve()
                            .onStatus(
                                    status -> status.is4xxClientError(),
                                    (request, resp) -> {
                                        System.out.println(
                                                "Inline handler: Caught 4xx error - "
                                                        + resp.getStatusCode());
                                        throw new RuntimeException(
                                                "Bad request detected: " + resp.getStatusCode());
                                    })
                            .body(String.class);
            System.out.println("Response: " + response);
        } catch (RuntimeException e) {
            System.out.println("Caught inline handled exception:");
            System.out.println("  Message: " + e.getMessage());
        }
    }

    /**
     * Demonstrates error handling with fallback values. Shows how to provide default responses when
     * errors occur.
     */
    public void demonstrateErrorRecovery() {
        System.out.println("\n=== Error Recovery Demo ===");

        System.out.println("\n1. Error recovery with fallback value:");
        String result = getDataWithFallback();
        System.out.println("Result: " + result);

        System.out.println("\n2. Error recovery with retry:");
        String retryResult = getDataWithRetry();
        System.out.println("Result after retry: " + retryResult);
    }

    /**
     * Demonstrates returning a fallback value when an error occurs.
     *
     * @return the response body or a fallback value
     */
    private String getDataWithFallback() {
        try {
            return errorHandlingRestClient.get().uri("/status/500").retrieve().body(String.class);
        } catch (ServerErrorException e) {
            System.out.println("Error occurred, returning fallback value");
            return "Fallback data: Service temporarily unavailable";
        }
    }

    /**
     * Demonstrates retry logic when an error occurs. In a real application, this could use more
     * sophisticated retry mechanisms like Spring Retry or Resilience4j.
     *
     * @return the response body after retry
     */
    private String getDataWithRetry() {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                System.out.println("Attempt " + attempt + " of " + maxRetries);
                return errorHandlingRestClient
                        .get()
                        .uri("/status/500")
                        .retrieve()
                        .body(String.class);
            } catch (ServerErrorException e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt >= maxRetries) {
                    System.out.println("Max retries reached, returning fallback");
                    return "Fallback data: Service unavailable after " + maxRetries + " retries";
                }
                // In real apps, add exponential backoff here
                try {
                    Thread.sleep(1000 * attempt); // Simple linear backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "Fallback data: Retry interrupted";
                }
            }
        }

        return "Fallback data: Unexpected retry exit";
    }
}
