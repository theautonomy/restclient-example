package com.example.restclientdemo.service;

import java.util.Map;

import com.example.restclientdemo.model.HttpBinResponse;
import com.example.restclientdemo.model.User;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import tools.jackson.databind.json.JsonMapper;

@Service
public class HttpBinService {

    private final RestClient defaultRestClient;
    private final RestClient customRestClient;
    private final JsonMapper objectMapper;

    public HttpBinService(
            @Qualifier("defaultRestClient") RestClient defaultRestClient,
            @Qualifier("customRestClient") RestClient customRestClient,
            JsonMapper objectMapper) {
        this.defaultRestClient = defaultRestClient;
        this.customRestClient = customRestClient;
        this.objectMapper = objectMapper;
    }

    // GET Methods Demo
    public HttpBinResponse simpleGet() {
        System.out.println("\n=== Simple GET Request ===");
        return defaultRestClient.get().uri("/get").retrieve().body(HttpBinResponse.class);
    }

    public HttpBinResponse getWithQueryParams() {
        System.out.println("\n=== GET with Query Parameters ===");
        return defaultRestClient
                .get()
                .uri("/get?name=John&age=30&city=New York")
                .retrieve()
                .body(HttpBinResponse.class);
    }

    public HttpBinResponse getWithUriVariables() {
        System.out.println("\n=== GET with URI Variables ===");
        return defaultRestClient
                .get()
                .uri("/get?name={name}&age={age}", "Jane", 25)
                .retrieve()
                .body(HttpBinResponse.class);
    }

    public HttpBinResponse getWithCustomHeaders() {
        System.out.println("\n=== GET with Custom Headers ===");
        return defaultRestClient
                .get()
                .uri("/get")
                .header("X-Custom-Header", "Custom-Value")
                .header("X-Request-ID", "12345")
                .retrieve()
                .body(HttpBinResponse.class);
    }

    public HttpBinResponse postJson() {
        System.out.println("\n=== POST with JSON Body ===");
        User user = new User("Alice", "alice@example.com", 28);

        return defaultRestClient
                .post()
                .uri("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .body(HttpBinResponse.class);
    }

    public HttpBinResponse postFormData() {
        System.out.println("\n=== POST with Form Data ===");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "johndoe");
        formData.add("password", "secret123");
        formData.add("email", "john@example.com");

        return defaultRestClient
                .post()
                .uri("/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(HttpBinResponse.class);
    }

    public HttpBinResponse postWithCustomClient() {
        System.out.println("\n=== POST with Custom RestClient ===");
        Map<String, Object> data =
                Map.of(
                        "message",
                        "Hello from custom client",
                        "timestamp",
                        System.currentTimeMillis());

        return customRestClient
                .post()
                .uri("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .body(data)
                .retrieve()
                .body(HttpBinResponse.class);
    }

    // PUT Method Demo
    public HttpBinResponse putData() {
        System.out.println("\n=== PUT Request ===");
        User updatedUser = new User("Bob Updated", "bob.updated@example.com", 35);

        return defaultRestClient
                .put()
                .uri("/put")
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedUser)
                .retrieve()
                .body(HttpBinResponse.class);
    }

    // PATCH Method Demo
    public HttpBinResponse patchData() {
        System.out.println("\n=== PATCH Request ===");
        Map<String, Object> partialUpdate = Map.of("age", 36);

        return defaultRestClient
                .patch()
                .uri("/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(partialUpdate)
                .retrieve()
                .body(HttpBinResponse.class);
    }

    // DELETE Method Demo
    public HttpBinResponse deleteResource() {
        System.out.println("\n=== DELETE Request ===");
        return defaultRestClient.delete().uri("/delete").retrieve().body(HttpBinResponse.class);
    }

    // Response Entity Demo
    public ResponseEntity<HttpBinResponse> getWithResponseEntity() {
        System.out.println("\n=== GET with ResponseEntity ===");
        return defaultRestClient.get().uri("/get").retrieve().toEntity(HttpBinResponse.class);
    }

    // Generic Type Demo
    public Map<String, Object> getAsMap() {
        System.out.println("\n=== GET as Generic Map ===");
        return defaultRestClient
                .get()
                .uri("/get")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // Error Handling Demo
    public void demonstrateErrorHandling() {
        System.out.println("\n=== Error Handling Demo ===");

        try {
            // This will return 404
            defaultRestClient.get().uri("/status/404").retrieve().body(String.class);
        } catch (RestClientResponseException e) {
            System.out.println("Caught RestClientResponseException:");
            System.out.println("   Status Code: " + e.getStatusCode());
            System.out.println("   Status Text: " + e.getStatusText());
            System.out.println("   Response Body: " + e.getResponseBodyAsString());
        }

        try {
            // This will return 500
            defaultRestClient
                    .get()
                    .uri("/status/500")
                    .retrieve()
                    .onStatus(
                            status -> status.is5xxServerError(),
                            (request, response) -> {
                                throw new RuntimeException(
                                        "Server error occurred: " + response.getStatusCode());
                            })
                    .body(String.class);
        } catch (RuntimeException e) {
            System.out.println("Caught custom exception: " + e.getMessage());
        }
    }

    // Status Code Handling Demo
    public void demonstrateStatusHandling() {
        System.out.println("\n=== Status Code Handling Demo ===");

        String result =
                defaultRestClient
                        .get()
                        .uri("/status/201")
                        .retrieve()
                        .onStatus(
                                status -> status.is2xxSuccessful(),
                                (request, response) -> {
                                    System.out.println(
                                            "Success! Status: " + response.getStatusCode());
                                })
                        .onStatus(
                                status -> status.is4xxClientError(),
                                (request, response) -> {
                                    System.out.println("Client error: " + response.getStatusCode());
                                })
                        .body(String.class);

        System.out.println("Response body: " + result);
    }

    // Exchange Method Demo - Full control over request and response
    public HttpBinResponse demonstrateExchange() {
        System.out.println("\n=== Exchange Method Demo ===");
        System.out.println("Using exchange() for full control over request and response handling");

        return defaultRestClient
                .get()
                .uri("/get?id=123&name=TestUser")
                .accept(MediaType.APPLICATION_JSON)
                .exchange(
                        (request, response) -> {
                            System.out.println("Inside exchange handler:");
                            System.out.println("  Request URI: " + request.getURI());
                            System.out.println("  Response Status: " + response.getStatusCode());
                            // System.out.println("  Response Headers: " +
                            // response.getHeaders().keySet());

                            // Manual error handling - status handlers are NOT automatically
                            // applied
                            if (response.getStatusCode().is4xxClientError()) {
                                throw new RuntimeException(
                                        "Client error: "
                                                + response.getStatusCode()
                                                + ", Headers: "
                                                + response.getHeaders());
                            } else if (response.getStatusCode().is5xxServerError()) {
                                throw new RuntimeException(
                                        "Server error: " + response.getStatusCode());
                            }

                            // Custom response conversion
                            try {
                                HttpBinResponse result =
                                        objectMapper.readValue(
                                                response.getBody(), HttpBinResponse.class);
                                System.out.println(
                                        "  Successfully converted response to HttpBinResponse");
                                return result;
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to parse response", e);
                            }
                        });
    }

    // Exchange with error simulation
    public String demonstrateExchangeWithError() {
        System.out.println("\n=== Exchange Method with Error Handling ===");

        try {
            return defaultRestClient
                    .get()
                    .uri("/status/404") // This will return 404
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange(
                            (request, response) -> {
                                System.out.println("Response Status: " + response.getStatusCode());

                                // With exchange(), we manually handle all status codes
                                if (response.getStatusCode().is4xxClientError()) {
                                    System.out.println("  Handling 4xx error in exchange method");
                                    throw new RuntimeException(
                                            "Custom 4xx error: " + response.getStatusCode());
                                }

                                return "Success";
                            });
        } catch (RuntimeException e) {
            System.out.println("Caught exception: " + e.getMessage());
            return "Error handled: " + e.getMessage();
        }
    }

    // Comparison: Exchange vs Retrieve
    public void compareExchangeVsRetrieve() {
        System.out.println("\n=== Comparison: Exchange vs Retrieve ===");

        // Using retrieve() - automatic status code handling
        System.out.println("\n1. Using retrieve():");
        try {
            defaultRestClient.get().uri("/get").retrieve().body(HttpBinResponse.class);
            System.out.println("   retrieve() automatically applies default status handlers");
        } catch (Exception e) {
            System.out.println("   Error: " + e.getMessage());
        }

        // Using exchange() - manual status code handling
        System.out.println("\n2. Using exchange():");
        try {
            HttpBinResponse result =
                    defaultRestClient
                            .get()
                            .uri("/get")
                            .exchange(
                                    (request, response) -> {
                                        System.out.println(
                                                "   exchange() requires manual handling of status codes");
                                        System.out.println(
                                                "   You have direct access to request: "
                                                        + request.getURI());
                                        System.out.println(
                                                "   You have direct access to response: "
                                                        + response.getStatusCode());

                                        // Must manually convert response
                                        return objectMapper.readValue(
                                                response.getBody(), HttpBinResponse.class);
                                    });
            System.out.println("   Success: " + result);
        } catch (Exception e) {
            System.out.println("   Error: " + e.getMessage());
        }

        System.out.println("\nKey Differences:");
        System.out.println("  - retrieve(): Status handlers applied automatically");
        System.out.println("  - exchange(): Full manual control, no automatic error handling");
        System.out.println("  - exchange(): Direct access to both request and response objects");
        System.out.println("  - exchange(): Useful for complex response processing scenarios");
    }

    // Authentication Demo (Basic Auth)
    public String demonstrateBasicAuth() {
        System.out.println("\n=== Basic Authentication Demo ===");

        try {
            return defaultRestClient
                    .get()
                    .uri("/basic-auth/user/passwd")
                    .header("Authorization", "Basic dXNlcjpwYXNzd2Q=") // user:passwd encoded
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            System.out.println("Authentication failed: " + e.getStatusCode());
            return null;
        }
    }

    // Headers Demo
    public Map<String, String> getHeaders() {
        System.out.println("\n=== Headers Demo ===");

        HttpBinResponse response =
                defaultRestClient
                        .get()
                        .uri("/headers")
                        .header("X-Test-Header-1", "Value1")
                        .header("X-Test-Header-2", "Value2")
                        .retrieve()
                        .body(HttpBinResponse.class);

        return response != null ? response.getHeaders() : null;
    }

    // IP Address Demo
    public String getIpAddress() {
        System.out.println("\n=== IP Address Demo ===");

        Map<String, Object> response =
                defaultRestClient
                        .get()
                        .uri("/ip")
                        .retrieve()
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        return response != null ? (String) response.get("origin") : null;
    }

    // User Agent Demo
    public String getUserAgent() {
        System.out.println("\n=== User Agent Demo ===");

        Map<String, Object> response =
                defaultRestClient
                        .get()
                        .uri("/user-agent")
                        .retrieve()
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        return response != null ? (String) response.get("user-agent") : null;
    }
}
