package com.example.restclientdemo.runner;

import java.util.Map;

import com.example.restclientdemo.model.HttpBinResponse;
import com.example.restclientdemo.service.HttpBinService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements CommandLineRunner {

    private final HttpBinService httpBinService;

    public DemoRunner(HttpBinService httpBinService) {
        this.httpBinService = httpBinService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🌟 Starting Spring Boot RestClient Demo 🌟");
        System.out.println("=".repeat(50));

        // GET Requests
        runDemo(
                "Simple GET",
                () -> {
                    HttpBinResponse response = httpBinService.simpleGet();
                    System.out.println("URL: " + response.getUrl());
                    System.out.println("Origin: " + response.getOrigin());
                });

        runDemo(
                "GET with Query Parameters",
                () -> {
                    HttpBinResponse response = httpBinService.getWithQueryParams();
                    System.out.println("Args: " + response.getArgs());
                });

        runDemo(
                "GET with URI Variables",
                () -> {
                    HttpBinResponse response = httpBinService.getWithUriVariables();
                    System.out.println("Args: " + response.getArgs());
                });

        runDemo(
                "GET with Custom Headers",
                () -> {
                    HttpBinResponse response = httpBinService.getWithCustomHeaders();
                    System.out.println(
                            "Custom headers in response: "
                                    + response.getHeaders().entrySet().stream()
                                            .filter(e -> e.getKey().startsWith("X-"))
                                            .toList());
                });

        // POST Requests
        runDemo(
                "POST with JSON",
                () -> {
                    HttpBinResponse response = httpBinService.postJson();
                    System.out.println("JSON Data: " + response.getJson());
                });

        runDemo(
                "POST with Form Data",
                () -> {
                    HttpBinResponse response = httpBinService.postFormData();
                    System.out.println("Form Data: " + response.getForm());
                });

        runDemo(
                "POST with Custom Client",
                () -> {
                    HttpBinResponse response = httpBinService.postWithCustomClient();
                    System.out.println("JSON Data: " + response.getJson());
                    System.out.println(
                            "Custom Header: " + response.getHeaders().get("Custom-Header"));
                });

        // Other HTTP Methods
        runDemo(
                "PUT Request",
                () -> {
                    HttpBinResponse response = httpBinService.putData();
                    System.out.println("JSON Data: " + response.getJson());
                });

        runDemo(
                "PATCH Request",
                () -> {
                    HttpBinResponse response = httpBinService.patchData();
                    System.out.println("JSON Data: " + response.getJson());
                });

        runDemo(
                "DELETE Request",
                () -> {
                    HttpBinResponse response = httpBinService.deleteResource();
                    System.out.println("URL: " + response.getUrl());
                });

        // Advanced Features
        runDemo(
                "Response Entity",
                () -> {
                    ResponseEntity<HttpBinResponse> response =
                            httpBinService.getWithResponseEntity();
                    System.out.println("Status Code: " + response.getStatusCode());
                    System.out.println(
                            "Headers: " + response.getHeaders().getFirst("Content-Type"));
                    System.out.println("Body URL: " + response.getBody().getUrl());
                });

        runDemo(
                "Generic Map Response",
                () -> {
                    Map<String, Object> response = httpBinService.getAsMap();
                    System.out.println("Response as Map: " + response.keySet());
                });

        // Utility Endpoints
        runDemo(
                "IP Address",
                () -> {
                    String ip = httpBinService.getIpAddress();
                    System.out.println("Your IP: " + ip);
                });

        runDemo(
                "User Agent",
                () -> {
                    String userAgent = httpBinService.getUserAgent();
                    System.out.println("User Agent: " + userAgent);
                });

        runDemo(
                "Headers Info",
                () -> {
                    Map<String, String> headers = httpBinService.getHeaders();
                    System.out.println("Request Headers Count: " + headers.size());
                    System.out.println("User-Agent: " + headers.get("User-Agent"));
                });

        // Authentication
        runDemo(
                "Basic Authentication",
                () -> {
                    HttpBinResponse response = httpBinService.demonstrateBasicAuth();
                    if (response != null) {
                        System.out.println("✅ Authentication successful!");
                        System.out.println("Response: " + response.getUrl());
                    } else {
                        System.out.println("❌ Authentication failed");
                    }
                });

        // Error Handling
        runDemo(
                "Error Handling",
                () -> {
                    httpBinService.demonstrateErrorHandling();
                });

        runDemo(
                "Status Code Handling",
                () -> {
                    httpBinService.demonstrateStatusHandling();
                });

        System.out.println("\n🎉 Demo completed successfully! 🎉");
    }

    private void runDemo(String title, Runnable demo) {
        try {
            System.out.println("\n" + "─".repeat(50));
            System.out.println("🔥 " + title);
            System.out.println("─".repeat(50));
            demo.run();
            Thread.sleep(1000); // Small delay between requests to be nice to httpbin.org
        } catch (Exception e) {
            System.err.println("❌ Error in " + title + ": " + e.getMessage());
        }
    }
}
