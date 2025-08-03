package com.example.app;

import java.util.*;

/**
 * Demo class showing usage of RestClient
 */
public class RestClientDemo {
    public static void main(String[] args) {
        System.out.println("=== RestClient Demo ===\n");
        
        // Create RestClient with base URL
        RestClient client = RestClient.builder()
            .baseUrl("https://api.example.com")
            .defaultHeader("User-Agent", "RestClient-Demo/1.0")
            .defaultHeader("Accept", "application/json")
            .build();
        
        // Example 1: Simple GET request
        System.out.println("1. Simple GET request:");
        String response1 = client.get()
            .uri("/users/{0}", 123)
            .header("Authorization", "Bearer token123")
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response1);
        System.out.println();
        
        // Example 2: POST request with body
        System.out.println("2. POST request with body:");
        Map<String, Object> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("email", "john@example.com");
        
        ResponseEntity<String> response2 = client.post()
            .uri("/users")
            .header("Content-Type", "application/json")
            .body(user)
            .retrieve()
            .toEntity(String.class);
        
        System.out.println("Status: " + response2.getStatusCode());
        System.out.println("Response: " + response2.getBody());
        System.out.println();
        
        // Example 3: PUT request
        System.out.println("3. PUT request:");
        user.put("id", 123);
        user.put("name", "John Smith");
        
        String response3 = client.put()
            .uri("/users/{0}", 123)
            .body(user)
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response3);
        System.out.println();
        
        // Example 4: DELETE request
        System.out.println("4. DELETE request:");
        ResponseEntity<Void> response4 = client.delete()
            .uri("/users/{0}", 123)
            .retrieve()
            .toBodilessEntity();
        System.out.println("Status: " + response4.getStatusCode());
        System.out.println();
        
        // Example 5: Using method() with custom headers
        System.out.println("5. Custom method with headers:");
        HttpHeaders customHeaders = new HttpHeaders();
        customHeaders.set("X-Custom-Header", "custom-value");
        customHeaders.set("X-Request-ID", "req-12345");
        
        String response5 = client.method(HttpMethod.GET)
            .uri("/health")
            .headers(customHeaders)
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response5);
        System.out.println();
        
        // Example 6: Create simple client without base URL
        System.out.println("6. Simple client without base URL:");
        RestClient simpleClient = RestClient.create();
        String response6 = simpleClient.get()
            .uri("https://httpbin.org/get")
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response6);
    }
}
