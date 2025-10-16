package com.example.restclientdemo.config;

import com.example.restclientdemo.client.UserClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration for JSONPlaceholder API testing.
 *
 * <p>Creates RestClient instances configured to use https://jsonplaceholder.typicode.com for
 * testing real HTTP operations.
 */
@Configuration
public class JSONPlaceholderConfig {

    /**
     * RestClient configured for JSONPlaceholder API. Used by UserService for testing real API
     * operations.
     */
    @Bean
    public RestClient jsonPlaceholderRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    /** Declarative HTTP Interface client for JSONPlaceholder users endpoint. */
    @Bean
    public UserClient jsonPlaceholderUserClient(RestClient.Builder builder) {
        RestClient restClient =
                builder.baseUrl("https://jsonplaceholder.typicode.com")
                        .defaultHeader("Accept", "application/json")
                        .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserClient.class);
    }
}
