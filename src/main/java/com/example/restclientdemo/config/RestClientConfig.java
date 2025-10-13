package com.example.restclientdemo.config;

import com.example.restclientdemo.client.HttpBinClient;
import com.example.restclientdemo.handler.CustomResponseErrorHandler;
import com.example.restclientdemo.resolver.SearchQueryArgumentResolver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration class for RestClient and HTTP Interface setup.
 *
 * <p>This configuration demonstrates: - Multiple RestClient bean configurations with different
 * settings - HTTP Interface proxy creation using HttpServiceProxyFactory - Custom argument resolver
 * registration (SearchQueryArgumentResolver) - Request/response logging interceptor - Custom error
 * handling with ResponseErrorHandler
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient defaultRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://httpbin.org")
                .defaultHeader("User-Agent", "Spring-RestClient-Demo/1.0")
                .defaultHeader("Accept", "application/json")
                .requestInterceptor(loggingInterceptor())
                .build();
    }

    @Bean
    public RestClient customRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://httpbin.org")
                .defaultHeader("Custom-Header", "Demo-Value")
                .requestInterceptor(loggingInterceptor())
                .build();
    }

    /**
     * RestClient configured with custom error handler.
     *
     * <p>This client demonstrates using ResponseErrorHandler to: - Customize error detection logic
     * - Throw custom exceptions for different error types - Extract and include response body in
     * exceptions - Provide detailed error context for debugging
     *
     * @param builder the RestClient builder
     * @return RestClient with custom error handling
     */
    @Bean
    public RestClient errorHandlingRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://httpbin.org")
                .defaultHeader("User-Agent", "Spring-RestClient-Demo/1.0")
                .defaultHeader("Accept", "application/json")
                .requestInterceptor(loggingInterceptor())
                .defaultStatusHandler(new CustomResponseErrorHandler())
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            System.out.println(
                    "Making request to: " + request.getMethod() + " " + request.getURI());
            System.out.println("Headers: " + request.getHeaders());
            if (body.length > 0) {
                System.out.println("Body: " + new String(body));
            }

            var response = execution.execute(request, body);

            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Headers: " + response.getHeaders());
            System.out.println("---");

            return response;
        };
    }

    /**
     * Creates an HTTP Interface client proxy with custom argument resolver.
     *
     * <p>The HttpServiceProxyFactory is configured with: - RestClientAdapter wrapping the default
     * RestClient - SearchQueryArgumentResolver for handling SearchQuery method parameters
     *
     * <p>This allows the HttpBinClient interface methods to use SearchQuery objects as parameters,
     * which will be automatically converted to query parameters by the custom resolver.
     *
     * @param restClient the RestClient to use for HTTP requests
     * @return the proxy implementation of HttpBinClient
     */
    @Bean
    public HttpBinClient httpBinClient(@Qualifier("defaultRestClient") RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter)
                        .customArgumentResolver(new SearchQueryArgumentResolver())
                        .build();
        return factory.createClient(HttpBinClient.class);
    }
}
