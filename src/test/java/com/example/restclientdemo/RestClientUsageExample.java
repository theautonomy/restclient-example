package com.example.restclientdemo;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class RestClientUsageExample {

    // Example 1: Basic RestClient with static headers
    public RestClient createBasicRestClient() {
        return RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("User-Agent", "MyApp/1.0")
                .build();
    }

    // Example 2: RestClient with Consumer-based headers (dynamic configuration)
    public RestClient createAdvancedRestClient(String environment, boolean includeAuth) {
        return RestClient.builder()
                .baseUrl("https://api.example.com")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeaders(
                        headers -> {
                            // Conditionally add headers based on environment
                            if ("production".equals(environment)) {
                                headers.set("X-Environment", "PROD");
                                headers.set("X-Rate-Limit", "1000");
                            } else {
                                headers.set("X-Environment", "DEV");
                                headers.set("X-Rate-Limit", "100");
                            }

                            // Conditionally add authentication
                            if (includeAuth) {
                                headers.setBasicAuth("apiuser", "apipass123");
                            }

                            // Add timestamp for tracking
                            headers.set(
                                    "X-Request-Time", String.valueOf(System.currentTimeMillis()));

                            // Custom header with computed value
                            headers.set("X-Client-ID", generateClientId());
                        })
                .build();
    }

    // Example 3: Multiple RestClients for different APIs
    public class ApiClients {
        private final RestClient jsonPlaceholderClient;
        private final RestClient githubClient;
        private final RestClient internalApiClient;

        public ApiClients() {
            // Client for JSONPlaceholder API
            this.jsonPlaceholderClient =
                    RestClient.builder()
                            .baseUrl("https://jsonplaceholder.typicode.com")
                            .defaultHeader("Accept", "application/json")
                            .build();

            // Client for GitHub API
            this.githubClient =
                    RestClient.builder()
                            .baseUrl("https://api.github.com")
                            .defaultHeaders(
                                    headers -> {
                                        headers.set("Accept", "application/vnd.github.v3+json");
                                        headers.set("User-Agent", "MyApp-GitHub-Client");
                                        // In real scenario, you'd get token from config/environment
                                        // headers.setBearerAuth("your_github_token");
                                    })
                            .build();

            // Client for internal API with complex authentication
            this.internalApiClient =
                    RestClient.builder()
                            .baseUrl("https://internal-api.company.com")
                            .defaultHeaders(
                                    headers -> {
                                        headers.set("Content-Type", "application/json");
                                        headers.set("Accept", "application/json");

                                        // Custom authentication scheme
                                        String apiKey = getApiKeyFromConfig();
                                        String signature = generateSignature(apiKey);
                                        headers.set("X-API-Key", apiKey);
                                        headers.set("X-Signature", signature);

                                        // Add correlation ID for tracing
                                        headers.set(
                                                "X-Correlation-ID",
                                                java.util.UUID.randomUUID().toString());
                                    })
                            .build();
        }

        // Demonstrate using JSONPlaceholder client
        public List<Post> getAllPosts() {
            ResponseEntity<Post[]> response =
                    jsonPlaceholderClient.get().uri("/posts").retrieve().toEntity(Post[].class);

            return List.of(response.getBody());
        }

        // Demonstrate using GitHub client
        public GitHubUser getGitHubUser(String username) {
            return githubClient
                    .get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .body(GitHubUser.class);
        }

        // Demonstrate using internal API client with additional headers
        public String createUser(User user) {
            return internalApiClient
                    .post()
                    .uri("/users")
                    .header(
                            "X-Request-Type",
                            "CREATE_USER") // Additional header for this specific request
                    .body(user)
                    .retrieve()
                    .body(String.class);
        }
    }

    // Example 4: Demonstrating header inspection and debugging
    public void demonstrateHeaderInspection() {
        RestClient debugClient =
                RestClient.builder()
                        .baseUrl("https://httpbin.org")
                        .defaultHeader("Static-Header", "StaticValue")
                        .defaultHeaders(
                                headers -> {
                                    System.out.println("=== Configuring Default Headers ===");

                                    // Add headers and show what's being set
                                    headers.set("Dynamic-Header", "DynamicValue");
                                    System.out.println("Added Dynamic-Header: DynamicValue");

                                    headers.setBasicAuth("testuser", "testpass");
                                    System.out.println("Added Basic Auth for testuser");

                                    // You can inspect existing headers
                                    System.out.println("Current headers being configured:");
                                    headers.forEach(
                                            (key, values) -> {
                                                System.out.println("  " + key + ": " + values);
                                            });
                                })
                        .build();

        // Make a request to httpbin.org/headers to see what headers are sent
        Map<String, Object> response = debugClient.get().uri("/headers").retrieve().body(Map.class);

        System.out.println("=== Headers received by server ===");
        System.out.println(response);
    }

    // Example 5: Builder pattern showing step-by-step configuration
    public RestClient createStepByStepClient() {
        System.out.println("Step 1: Creating builder");
        RestClient.Builder builder = RestClient.builder();

        System.out.println("Step 2: Setting base URL");
        builder.baseUrl("https://api.example.com");

        System.out.println("Step 3: Adding first default header");
        builder.defaultHeader("Content-Type", "application/json");

        System.out.println("Step 4: Adding second default header");
        builder.defaultHeader("Accept", "application/json");

        System.out.println("Step 5: Using defaultHeaders consumer");
        builder.defaultHeaders(
                headers -> {
                    System.out.println(
                            "  Inside consumer - current header count: " + headers.size());

                    headers.set("X-Custom-Header", "CustomValue");
                    System.out.println("  Added X-Custom-Header");

                    headers.set("X-Timestamp", String.valueOf(System.currentTimeMillis()));
                    System.out.println("  Added X-Timestamp");

                    System.out.println("  Final header count in consumer: " + headers.size());
                });

        System.out.println("Step 6: Building final RestClient");
        RestClient client = builder.build();

        System.out.println("RestClient created successfully!");
        return client;
    }

    // Utility methods
    private String generateClientId() {
        return "CLIENT-" + System.currentTimeMillis();
    }

    private String getApiKeyFromConfig() {
        // In real application, this would come from application.properties or environment
        return "api-key-12345";
    }

    private String generateSignature(String apiKey) {
        // In real application, this would be a proper HMAC signature
        return "signature-" + apiKey.hashCode();
    }

    // DTOs for examples
    public static class Post {
        private int id;
        private int userId;
        private String title;
        private String body;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public static class GitHubUser {
        private String login;
        private String name;
        private String company;

        // Getters and setters
        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }

    public static class User {
        private String username;
        private String email;

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
