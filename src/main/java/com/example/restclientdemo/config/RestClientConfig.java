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

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient defaultRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://httpbin.org")
                .defaultHeader("User-Agent", "Spring-RestClient-Demo/1.0")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("what", "whatever")
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

    /** RestClient configured with custom error handler. */
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

    /** Creates an HTTP Interface client proxy with custom argument resolver. */
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
