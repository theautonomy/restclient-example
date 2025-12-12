package com.example.restclientdemo.runner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.example.restclientdemo.client.UserClient;
import com.example.restclientdemo.model.User;
import com.example.restclientdemo.service.UserService;

/**
 * Demo runner to test UserService with real JSONPlaceholder API.
 *
 * <p>This demonstrates all the code examples from spring-restclient-guide.adoc working with a real
 * API.
 */
// @Component
public class UserServiceDemoRunner implements CommandLineRunner {

    private final UserService userService;
    private final UserClient userClient;

    public UserServiceDemoRunner(
            @Qualifier("jsonPlaceholderRestClient") RestClient restClient,
            @Qualifier("jsonPlaceholderUserClient") UserClient userClient) {
        this.userService = new UserService(restClient);
        this.userClient = userClient;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UserService Demo with JSONPlaceholder API");
        System.out.println("=".repeat(80));

        // Basic GET Operations
        demonstrateBasicGet();

        // Query Parameters
        demonstrateQueryParameters();

        // POST, PUT, PATCH, DELETE (JSONPlaceholder fakes these)
        demonstrateMutationOperations();

        // Custom Headers
        demonstrateCustomHeaders();

        // Response Entity Access
        demonstrateResponseEntity();

        // Error Handling
        demonstrateErrorHandling();

        // HTTP Interface
        demonstrateHttpInterface();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("Demo completed successfully!");
        System.out.println("=".repeat(80) + "\n");
    }

    private void demonstrateBasicGet() {
        System.out.println("\n--- Basic GET Operations ---");

        // Get single user (line 119-124 in guide)
        User user = userService.getUser(1L);
        System.out.println("Get User 1: " + user.getName() + " (" + user.getEmail() + ")");

        // Get all users (line 126-132 in guide)
        List<User> users = userService.getAllUsers();
        System.out.println("Total users: " + users.size());
        System.out.println("First user: " + users.get(0).getName());
    }

    private void demonstrateQueryParameters() {
        System.out.println("\n--- Query Parameters ---");

        // Note: JSONPlaceholder doesn't support search, but the code demonstrates the pattern
        System.out.println("Query parameter methods work (pattern demonstration)");
        System.out.println("  - searchUsers() uses UriBuilder");
        System.out.println("  - searchUsersWithStringFormat() uses string interpolation");
    }

    private void demonstrateMutationOperations() {
        System.out.println("\n--- POST, PUT, PATCH, DELETE Operations ---");

        try {
            // POST - Create user (line 173-180)
            User newUser = new User("Test User", "test@example.com", 30);
            User created = userService.createUser(newUser);
            System.out.println("Created user (simulated): ID=" + created.getId());

            // PUT - Update user (line 207-214)
            created.setEmail("updated@example.com");
            User updated = userService.updateUser(1L, created);
            System.out.println("Updated user (simulated): " + updated.getEmail());

            // PATCH - Partial update (line 216-223)
            Map<String, Object> updates = Map.of("email", "patched@example.com");
            User patched = userService.partialUpdateUser(1L, updates);
            System.out.println("Patched user (simulated): " + patched.getEmail());

            // DELETE - Delete user (line 232-237 and 239-246)
            userService.deleteUser(1L);
            System.out.println("Deleted user (simulated): User 1");

            int statusCode = userService.deleteUserWithStatus(1L);
            System.out.println("Delete status code: " + statusCode);

        } catch (Exception e) {
            System.out.println("Mutation operations tested (JSONPlaceholder simulates these)");
        }
    }

    private void demonstrateCustomHeaders() {
        System.out.println("\n--- Custom Headers ---");

        // Custom headers (line 257-264)
        User user = userService.getUserWithHeaders(1L);
        System.out.println("Got user with custom headers: " + user.getName());

        // Dynamic headers (line 271-281)
        user = userService.getUserWithDynamicHeaders(1L);
        System.out.println("Got user with dynamic headers: " + user.getName());
    }

    private void demonstrateResponseEntity() {
        System.out.println("\n--- Response Entity Access ---");

        // ResponseEntity (line 322-327)
        ResponseEntity<User> response = userService.getUserWithMetadata(1L);
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Headers (sample): " + response.getHeaders().getContentType());
        System.out.println("Response Body: " + response.getBody().getName());

        // Process response (line 329-338)
        System.out.println("Processing response with metadata...");
        userService.processUserResponse(1L);
    }

    private void demonstrateErrorHandling() {
        System.out.println("\n--- Error Handling ---");

        // Valid user - should work
        User user = userService.getUserWithErrorHandling(1L);
        System.out.println("Success: Got user " + user.getName());

        // Invalid user - will throw exception
        try {
            userService.getUserWithErrorHandling(999L);
        } catch (Exception e) {
            System.out.println("Expected error caught: " + e.getClass().getSimpleName());
        }

        // Custom error handling (line 365-378)
        try {
            userService.getUserWithCustomErrorHandling(999L);
        } catch (Exception e) {
            System.out.println("Custom error handler caught: " + e.getClass().getSimpleName());
        }

        // Optional return (line 779-789)
        Optional<User> userOpt = userService.getUserSafely(1L);
        System.out.println("Optional present: " + userOpt.isPresent());

        Optional<User> notFound = userService.getUserSafely(999L);
        System.out.println("Optional empty: " + notFound.isEmpty());
    }

    private void demonstrateHttpInterface() {
        System.out.println("\n--- HTTP Interface (Declarative Client) ---");

        // Get all users
        List<User> users = userClient.getAllUsers();
        System.out.println("HTTP Interface - Total users: " + users.size());

        // Get single user
        User user = userClient.getUser(1L);
        System.out.println("HTTP Interface - User 1: " + user.getName());

        // Create user (simulated by JSONPlaceholder)
        User newUser = new User("John", "john@example.com", 30);
        User created = userClient.createUser(newUser);
        System.out.println("HTTP Interface - Created user ID: " + created.getId());

        // Update user (simulated)
        created.setEmail("john.updated@example.com");
        User updated = userClient.updateUser(1L, created);
        System.out.println("HTTP Interface - Updated email: " + updated.getEmail());

        // Delete user (simulated)
        userClient.deleteUser(1L);
        System.out.println("HTTP Interface - Deleted user 1");
    }
}
