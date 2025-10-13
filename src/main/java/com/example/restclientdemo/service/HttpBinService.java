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

@Service
public class HttpBinService {

    private final RestClient defaultRestClient;
    private final RestClient customRestClient;

    public HttpBinService(
            @Qualifier("defaultRestClient") RestClient defaultRestClient,
            @Qualifier("customRestClient") RestClient customRestClient) {
        this.defaultRestClient = defaultRestClient;
        this.customRestClient = customRestClient;
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

    // Authentication Demo (Basic Auth)
    public HttpBinResponse demonstrateBasicAuth() {
        System.out.println("\n=== Basic Authentication Demo ===");

        try {
            return defaultRestClient
                    .get()
                    .uri("/basic-auth/user/passwd")
                    .header("Authorization", "Basic dXNlcjpwYXNzd2Q=") // user:passwd encoded
                    .retrieve()
                    .body(HttpBinResponse.class);
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
