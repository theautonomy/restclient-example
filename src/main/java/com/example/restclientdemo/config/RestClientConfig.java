package com.example.restclientdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient defaultRestClient() {
        return RestClient.builder()
                .baseUrl("https://httpbin.org")
                .defaultHeader("User-Agent", "Spring-RestClient-Demo/1.0")
                .defaultHeader("Accept", "application/json")
                .requestInterceptor(loggingInterceptor())
                .build();
    }

    @Bean
    public RestClient customRestClient() {
        return RestClient.builder()
                .baseUrl("https://httpbin.org")
                .defaultHeader("Custom-Header", "Demo-Value")
                .requestInterceptor(loggingInterceptor())
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            System.out.println(
                    "🚀 Making request to: " + request.getMethod() + " " + request.getURI());
            System.out.println("📤 Headers: " + request.getHeaders());
            if (body.length > 0) {
                System.out.println("📄 Body: " + new String(body));
            }

            var response = execution.execute(request, body);

            System.out.println("📥 Response Status: " + response.getStatusCode());
            System.out.println("📋 Response Headers: " + response.getHeaders());
            System.out.println("---");

            return response;
        };
    }
}
