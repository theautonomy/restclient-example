package com.example.restclientdemo.service;

import java.util.Map;

import com.example.restclientdemo.client.HttpBinClient;
import com.example.restclientdemo.model.HttpBinResponse;
import com.example.restclientdemo.model.SearchQuery;
import com.example.restclientdemo.model.User;

import org.springframework.stereotype.Service;

/** Service demonstrating HTTP Interface usage with declarative client methods. */
@Service
public class HttpBinHttpInterfaceService {

    private final HttpBinClient httpBinClient;

    public HttpBinHttpInterfaceService(HttpBinClient httpBinClient) {
        this.httpBinClient = httpBinClient;
    }

    public void demonstrateHttpInterface() {
        System.out.println("\n=== HTTP Interface Demo ===");

        // Simple GET
        System.out.println("\n1. Simple GET request:");
        HttpBinResponse response = httpBinClient.get();
        System.out.println("Response: " + response);

        // GET with query parameters
        System.out.println("\n2. GET with query parameters:");
        Map<String, String> params =
                Map.of(
                        "name", "John Doe",
                        "age", "30");
        response = httpBinClient.getWithParams(params);
        System.out.println("Response: " + response);

        // POST with body
        System.out.println("\n3. POST with request body:");
        User user = new User("Alice", "alice@example.com", 25);
        response = httpBinClient.post(user);
        System.out.println("Response: " + response);

        // Basic Auth
        System.out.println("\n4. Basic Auth:");
        try {
            response = httpBinClient.basicAuth();
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("Auth failed: " + e.getMessage());
        }

        // Custom Argument Resolver - SearchQuery
        System.out.println("\n5. Custom Argument Resolver Demo:");
        demonstrateCustomArgumentResolver();
    }

    /**
     * Demonstrates custom HttpServiceArgumentResolver with SearchQuery.
     *
     * <p>The SearchQuery object is automatically converted to query parameters by the
     * SearchQueryArgumentResolver: - searchTerm -> q - limit -> limit - sortBy -> sort - ascending
     * -> order (asc/desc)
     */
    public void demonstrateCustomArgumentResolver() {

        // Simple search with just a search term
        System.out.println("\n1. Simple search with SearchQuery:");
        SearchQuery simpleQuery = SearchQuery.of("spring boot");
        HttpBinResponse response = httpBinClient.search(simpleQuery);
        System.out.println("Search Query: " + simpleQuery);
        System.out.println("Response args: " + response.getArgs());

        // Search with limit
        System.out.println("\n2. Search with limit:");
        SearchQuery limitedQuery = SearchQuery.of("java", 5);
        response = httpBinClient.search(limitedQuery);
        System.out.println("Search Query: " + limitedQuery);
        System.out.println("Response args: " + response.getArgs());

        // Search with all parameters
        System.out.println("\n3. Complete search with all parameters:");
        SearchQuery fullQuery = new SearchQuery("rest api", 20, "date", false);
        response = httpBinClient.search(fullQuery);
        System.out.println("Search Query: " + fullQuery);
        System.out.println("Response args: " + response.getArgs());
        System.out.println("Notice how SearchQuery fields are converted to query parameters:");
        System.out.println("  - searchTerm='rest api' -> q=rest api");
        System.out.println("  - limit=20 -> limit=20");
        System.out.println("  - sortBy='date' -> sort=date");
        System.out.println("  - ascending=false -> order=desc");
    }
}
