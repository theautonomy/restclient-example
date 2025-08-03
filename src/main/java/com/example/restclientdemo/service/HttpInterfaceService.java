package com.example.restclientdemo.service;

import java.util.Map;

import com.example.restclientdemo.client.HttpBinClient;
import com.example.restclientdemo.model.HttpBinResponse;
import com.example.restclientdemo.model.User;

import org.springframework.stereotype.Service;

@Service
public class HttpInterfaceService {

    private final HttpBinClient httpBinClient;

    public HttpInterfaceService(HttpBinClient httpBinClient) {
        this.httpBinClient = httpBinClient;
    }

    public void demonstrateHttpInterface() {
        System.out.println("\n=== HTTP Interface Demo ===");

        // Simple GET
        System.out.println("\n1. Simple GET request:");
        HttpBinResponse response = httpBinClient.get();
        System.out.println("✅ Response: " + response);

        // GET with query parameters
        System.out.println("\n2. GET with query parameters:");
        Map<String, String> params =
                Map.of(
                        "name", "John Doe",
                        "age", "30");
        response = httpBinClient.getWithParams(params);
        System.out.println("✅ Response: " + response);

        // POST with body
        System.out.println("\n3. POST with request body:");
        User user = new User("Alice", "alice@example.com", 25);
        response = httpBinClient.post(user);
        System.out.println("✅ Response: " + response);

        // Basic Auth
        System.out.println("\n4. Basic Auth:");
        try {
            response = httpBinClient.basicAuth();
            System.out.println("✅ Response: " + response);
        } catch (Exception e) {
            System.out.println("❌ Auth failed: " + e.getMessage());
        }
    }
}
