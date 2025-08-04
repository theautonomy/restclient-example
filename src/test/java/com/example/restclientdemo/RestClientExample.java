package com.example.restclientdemo;

import org.springframework.web.client.RestClient;

public class RestClientExample {

    public static void main(String[] args) {
        // Build a RestClient with default headers
        RestClient restClient =
                RestClient.builder()
                        .baseUrl("https://api.example.com")
                        .defaultHeader("Authorization", "Bearer your_access_token")
                        .defaultHeader("Content-Type", "application/json")
                        .defaultHeaders(
                                headers -> {
                                    // You can also use a Consumer to add/modify multiple headers
                                    headers.set("Custom-Header", "CustomValue");
                                    headers.setBasicAuth("username", "password");
                                })
                        .build();

        // Make a GET request using the configured client
        String responseBody = restClient.get().uri("/data").retrieve().body(String.class);

        System.out.println("Response: " + responseBody);
    }
}
