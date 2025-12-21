package com.example.restclientdemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.restclientdemo.exception.UserNotFoundException;
import com.example.restclientdemo.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * UserService Integration Tests with JSONPlaceholder using Jackson 3.
 *
 * <p>This test configures the JsonMapper with FAIL_ON_NULL_FOR_PRIMITIVES disabled using the
 * configureMessageConverters API with withJsonConverter().
 */
@DisplayName("UserService Integration Tests with JSONPlaceholder (Jackson 3)")
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Create Jackson 3 JsonMapper with custom configuration
        JsonMapper jsonMapper =
                JsonMapper.builder()
                        .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .build();

        // Build RestClient with custom Jackson 3 converter
        RestClient restClient =
                RestClient.builder()
                        .baseUrl("https://jsonplaceholder.typicode.com")
                        .configureMessageConverters(
                                builder ->
                                        builder.registerDefaults()
                                                .withJsonConverter(
                                                        new JacksonJsonHttpMessageConverter(
                                                                jsonMapper)))
                        .build();

        userService = new UserService(restClient);
    }

    // ===== Basic GET Operations =====

    @Test
    @DisplayName("Should get single user by id from JSONPlaceholder")
    void testGetUser() {
        User result = userService.getUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
    }

    @Test
    @DisplayName("Should get all users from JSONPlaceholder")
    void testGetAllUsers() {
        List<User> result = userService.getAllUsers();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(10); // JSONPlaceholder has 10 users
        assertThat(result.get(0).getName()).isEqualTo("Leanne Graham");
        assertThat(result.get(1).getName()).isEqualTo("Ervin Howell");
    }

    @Test
    @DisplayName("Should get user with id 2")
    void testGetUser2() {
        User result = userService.getUser(2L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Ervin Howell");
    }

    // ===== POST Operations =====

    @Test
    @DisplayName("Should create user with POST (simulated by JSONPlaceholder)")
    void testCreateUser() {
        User newUser = new User("Alice", "alice@example.com", 25);
        User result = userService.createUser(newUser);

        // JSONPlaceholder simulates creation and returns ID 11
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L);
    }

    @Test
    @DisplayName("Should create another user")
    void testCreateAnotherUser() {
        User newUser = new User("Bob", "bob@example.com", 30);
        User result = userService.createUser(newUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L); // JSONPlaceholder always returns 11
    }

    // ===== PUT Operations =====

    @Test
    @DisplayName("Should update user with PUT (simulated by JSONPlaceholder)")
    void testUpdateUser() {
        User updatedUser = new User("John Updated", "john.updated@example.com", 31);
        updatedUser.setId(1L);

        User result = userService.updateUser(1L, updatedUser);

        // JSONPlaceholder echoes back the data we sent
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should update user 5")
    void testUpdateUser5() {
        User updatedUser = new User("Test User", "test@example.com", 25);

        User result = userService.updateUser(5L, updatedUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(5L);
    }

    // ===== PATCH Operations =====

    @Test
    @DisplayName("Should partially update user with PATCH (simulated)")
    void testPartialUpdateUser() {
        Map<String, Object> updates = Map.of("email", "newemail@example.com");
        User result = userService.partialUpdateUser(1L, updates);

        // JSONPlaceholder returns the patched data
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should patch user with title field")
    void testPartialUpdateUserTitle() {
        Map<String, Object> updates = Map.of("name", "New Name");
        User result = userService.partialUpdateUser(2L, updates);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
    }

    // ===== DELETE Operations =====

    @Test
    @DisplayName("Should delete user (simulated by JSONPlaceholder)")
    void testDeleteUser() {
        // JSONPlaceholder simulates deletion, no exception should be thrown
        userService.deleteUser(1L);
        // If we get here without exception, the test passes
    }

    @Test
    @DisplayName("Should delete user and return status code")
    void testDeleteUserWithStatus() {
        int statusCode = userService.deleteUserWithStatus(1L);

        // JSONPlaceholder returns 200 OK for DELETE
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    @DisplayName("Should delete user 10")
    void testDeleteUser10() {
        userService.deleteUser(10L);
        // Successful deletion (simulated)
    }

    // ===== Custom Headers =====

    @Test
    @DisplayName("Should send request with custom headers")
    void testGetUserWithHeaders() {
        User result = userService.getUserWithHeaders(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Leanne Graham");
    }

    @Test
    @DisplayName("Should send request with dynamic headers")
    void testGetUserWithDynamicHeaders() {
        User result = userService.getUserWithDynamicHeaders(2L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ervin Howell");
    }

    // ===== Response Entity Access =====

    @Test
    @DisplayName("Should get user with response metadata")
    void testGetUserWithMetadata() {
        ResponseEntity<User> response = userService.getUserWithMetadata(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Leanne Graham");
        assertThat(response.getHeaders().getContentType()).isNotNull();
    }

    @Test
    @DisplayName("Should get user 3 with metadata")
    void testGetUser3WithMetadata() {
        ResponseEntity<User> response = userService.getUserWithMetadata(3L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should process user response with metadata")
    void testProcessUserResponse() {
        // This method prints to console, so we just verify it executes without errors
        userService.processUserResponse(1L);
        // If we reach here, the method executed successfully
    }

    // ===== Error Handling =====

    @Test
    @DisplayName("Should handle 404 error with basic error handling")
    void testGetUserWithErrorHandling_NotFound() {
        // JSONPlaceholder returns 404 for non-existent users
        assertThatThrownBy(() -> userService.getUserWithErrorHandling(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with id: 999");
    }

    @Test
    @DisplayName("Should successfully get user with error handling")
    void testGetUserWithErrorHandling_Success() {
        User result = userService.getUserWithErrorHandling(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Leanne Graham");
    }

    @Test
    @DisplayName("Should handle 404 with custom error handler")
    void testGetUserWithCustomErrorHandling_NotFound() {
        assertThatThrownBy(() -> userService.getUserWithCustomErrorHandling(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("Should successfully get user with custom error handling")
    void testGetUserWithCustomErrorHandling_Success() {
        User result = userService.getUserWithCustomErrorHandling(2L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ervin Howell");
    }

    @Test
    @DisplayName("Should get user 5 with custom error handling")
    void testGetUser5WithCustomErrorHandling() {
        User result = userService.getUserWithCustomErrorHandling(5L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(5L);
    }

    // ===== Optional Return Type =====

    @Test
    @DisplayName("Should return Optional with user when found")
    void testGetUserSafely_Found() {
        Optional<User> result = userService.getUserSafely(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Leanne Graham");
    }

    @Test
    @DisplayName("Should return empty Optional when user not found")
    void testGetUserSafely_NotFound() {
        Optional<User> result = userService.getUserSafely(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return Optional for user 10")
    void testGetUser10Safely() {
        Optional<User> result = userService.getUserSafely(10L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(10L);
    }

    // ===== Additional Integration Tests =====

    @Test
    @DisplayName("Should verify user 1 has all expected fields")
    void testUser1CompleteData() {
        User result = userService.getUser(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getUsername()).isEqualTo("Bret");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
        assertThat(result.getPhone()).isNotEmpty();
        assertThat(result.getWebsite()).isEqualTo("hildegard.org");
    }

    @Test
    @DisplayName("Should get all 10 users and verify first and last")
    void testGetAllUsersVerifyFirstAndLast() {
        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(10);
        assertThat(result.get(0).getName()).isEqualTo("Leanne Graham");
        assertThat(result.get(9).getName()).isEqualTo("Clementina DuBuque");
    }
}
