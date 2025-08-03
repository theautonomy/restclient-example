package com.example.app;

import java.util.*;

/**
 * Demo class showing usage of RestClient
 */
public class RestClientDemo {
    public static void main(String[] args) {
        System.out.println("=== RestClient Demo using httpbin.org ===\n");
        
        // Create RestClient with httpbin.org base URL
        RestClient client = RestClient.builder()
            .baseUrl("https://httpbin.org")
            .defaultHeader("User-Agent", "RestClient-Demo/1.0")
            .defaultHeader("Accept", "application/json")
            .build();
        
        // Example 1: GET request with query parameters
        System.out.println("1. GET request with query parameters:");
        String response1 = client.get()
            .uri("/get?name={0}&age={1}", "John", 30)
            .header("Custom-Header", "custom-value")
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response1);
        System.out.println();
        
        // Example 2: POST request with JSON body
        System.out.println("2. POST request with JSON body:");
        Map<String, Object> postData = new HashMap<>();
        postData.put("name", "John Doe");
        postData.put("email", "john@example.com");
        postData.put("age", 30);
        
        ResponseEntity<String> response2 = client.post()
            .uri("/post")
            .header("Content-Type", "application/json")
            .body(postData)
            .retrieve()
            .toEntity(String.class);
        
        System.out.println("Status: " + response2.getStatusCode());
        System.out.println("Response: " + response2.getBody());
        System.out.println();
        
        // Example 3: PUT request
        System.out.println("3. PUT request with body:");
        Map<String, Object> putData = new HashMap<>();
        putData.put("id", "123");
        putData.put("name", "John Smith");
        putData.put("updated_at", "2025-08-03");
        
        String response3 = client.put()
            .uri("/put")
            .header("Content-Type", "application/json")
            .body(putData)
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response3);
        System.out.println();
        
        // Example 4: DELETE request
        System.out.println("4. DELETE request with body:");
        Map<String, Object> deleteData = new HashMap<>();
        deleteData.put("id", "123");
        deleteData.put("reason", "User requested deletion");
        
        ResponseEntity<String> response4 = client.delete()
            .uri("/delete")
            .header("Content-Type", "application/json")
            .body(deleteData) // httpbin's delete endpoint accepts a body
            .retrieve()
            .toEntity(String.class);
        System.out.println("Status: " + response4.getStatusCode());
        System.out.println("Response: " + response4.getBody());
        System.out.println();
        
        // Example 5: PATCH request with custom headers
        System.out.println("5. PATCH request with custom headers:");
        Map<String, Object> patchData = new HashMap<>();
        patchData.put("updated_fields", Arrays.asList("status", "last_login"));
        patchData.put("status", "active");
        patchData.put("last_login", "2025-08-03T12:00:00Z");
        
        HttpHeaders customHeaders = new HttpHeaders();
        customHeaders.set("X-Request-ID", "patch-123");
        customHeaders.set("X-Patch-Type", "partial");
        
        String response5 = client.patch()
            .uri("/patch")
            .headers(customHeaders)
            .header("Content-Type", "application/json")
            .body(patchData)
            .retrieve()
            .body(String.class);
        System.out.println("Response: " + response5);
        System.out.println();
        
        // Example 6: HEAD request to check resource existence
        System.out.println("6. HEAD request:");
        ResponseEntity<Void> response6 = client.head()
            .uri("/get") // Using /get endpoint for HEAD request
            .retrieve()
            .toBodilessEntity();
        System.out.println("Status: " + response6.getStatusCode());
        System.out.println("Headers: " + response6.getHeaders());
        System.out.println();
    }
}
