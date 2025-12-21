package com.example.restclientdemo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.restclientdemo.client.UserClient;
import com.example.restclientdemo.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * UserHttpInterfaceService Integration Tests with JSONPlaceholder using Jackson 3.
 *
 * <p>This test manually configures RestClient with Jackson 3 (tools.jackson package) and disables
 * FAIL_ON_NULL_FOR_PRIMITIVES to handle null → primitive mapping.
 */
@DisplayName("UserHttpInterfaceService Integration Tests with JSONPlaceholder (Jackson 3)")
class UserHttpInterfaceServiceTest {

    private UserHttpInterfaceService service;

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

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();

        UserClient userClient = factory.createClient(UserClient.class);
        service = new UserHttpInterfaceService(userClient);
    }

    @Test
    @DisplayName("Should get all users via HTTP Interface Service")
    void testGetAllUsers() {
        List<User> result = service.getAllUsers();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(10); // JSONPlaceholder has 10 users
        assertThat(result.get(0).getName()).isEqualTo("Leanne Graham");
        assertThat(result.get(1).getName()).isEqualTo("Ervin Howell");
    }

    @Test
    @DisplayName("Should get single user by id via HTTP Interface Service")
    void testGetUser() {
        User result = service.getUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
    }

    @Test
    @DisplayName("Should get user 2 via HTTP Interface Service")
    void testGetUser2() {
        User result = service.getUser(2L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Ervin Howell");
    }

    @Test
    @DisplayName("Should get user 5 via HTTP Interface Service")
    void testGetUser5() {
        User result = service.getUser(5L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should create user via HTTP Interface Service (simulated)")
    void testCreateUser() {
        User newUser = new User("Charlie", "charlie@example.com", 35);
        User result = service.createUser(newUser);

        // JSONPlaceholder simulates creation and returns ID 11
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L);
    }

    @Test
    @DisplayName("Should create another user via HTTP Interface Service")
    void testCreateAnotherUser() {
        User newUser = new User("Diana", "diana@example.com", 28);
        User result = service.createUser(newUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L); // JSONPlaceholder always returns 11
    }

    @Test
    @DisplayName("Should update user via HTTP Interface Service (simulated)")
    void testUpdateUser() {
        User updatedUser = new User("John", "john.updated@example.com", 31);
        updatedUser.setId(1L);

        User result = service.updateUser(1L, updatedUser);

        // JSONPlaceholder echoes back the update
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should update user 3 via HTTP Interface Service")
    void testUpdateUser3() {
        User updatedUser = new User("Test", "test@example.com", 30);

        User result = service.updateUser(3L, updatedUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should delete user via HTTP Interface Service (simulated)")
    void testDeleteUser() {
        // JSONPlaceholder simulates deletion, no exception should be thrown
        service.deleteUser(1L);
        // If we get here without exception, the test passes
    }

    @Test
    @DisplayName("Should delete user 5 via HTTP Interface Service")
    void testDeleteUser5() {
        service.deleteUser(5L);
        // Successful deletion (simulated)
    }

    @Test
    @DisplayName("Should delete user 10 via HTTP Interface Service")
    void testDeleteUser10() {
        service.deleteUser(10L);
        // Successful deletion (simulated)
    }

    @Test
    @DisplayName("Should handle multiple sequential operations via service")
    void testMultipleSequentialOperations() {
        // Get existing user
        User retrieved = service.getUser(5L);
        assertThat(retrieved.getId()).isEqualTo(5L);

        // Create new user
        User created = service.createUser(new User("New User", "new@example.com", 20));
        assertThat(created.getId()).isEqualTo(11L);

        // Update user
        User updated = service.updateUser(1L, retrieved);
        assertThat(updated.getId()).isEqualTo(1L);

        // Delete user
        service.deleteUser(1L);
    }

    @Test
    @DisplayName("Should verify user 1 has all expected fields via service")
    void testUserWithAllFields() {
        User result = service.getUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getUsername()).isEqualTo("Bret");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
        assertThat(result.getPhone()).isEqualTo("1-770-736-8031 x56442");
        assertThat(result.getWebsite()).isEqualTo("hildegard.org");
    }

    @Test
    @DisplayName("Should get users 1 through 3 via service")
    void testMultipleUsers() {
        User user1 = service.getUser(1L);
        User user2 = service.getUser(2L);
        User user3 = service.getUser(3L);

        assertThat(user1.getName()).isEqualTo("Leanne Graham");
        assertThat(user2.getName()).isEqualTo("Ervin Howell");
        assertThat(user3.getName()).isEqualTo("Clementine Bauch");
    }

    @Test
    @DisplayName("Should verify last user (id 10) via service")
    void testLastUser() {
        User result = service.getUser(10L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("Clementina DuBuque");
    }

    @Test
    @DisplayName("Should get all users and verify count via service")
    void testGetAllUsersCount() {
        List<User> users = service.getAllUsers();

        assertThat(users).hasSize(10);
        // Verify all user IDs exist
        assertThat(users).extracting(User::getId).contains(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    }

    @Test
    @DisplayName("Should verify user emails are valid via service")
    void testUserEmailsValid() {
        User user1 = service.getUser(1L);
        User user2 = service.getUser(2L);

        assertThat(user1.getEmail()).contains("@");
        assertThat(user2.getEmail()).contains("@");
        assertThat(user1.getEmail()).isEqualTo("Sincere@april.biz");
        assertThat(user2.getEmail()).isEqualTo("Shanna@melissa.tv");
    }

    @Test
    @DisplayName("Should create multiple users sequentially via service")
    void testCreateMultipleUsers() {
        User user1 = service.createUser(new User("User1", "user1@test.com", 25));
        User user2 = service.createUser(new User("User2", "user2@test.com", 30));

        // Both should return ID 11 from JSONPlaceholder
        assertThat(user1.getId()).isEqualTo(11L);
        assertThat(user2.getId()).isEqualTo(11L);
    }

    @Test
    @DisplayName("Should update multiple users via service")
    void testUpdateMultipleUsers() {
        User update1 = service.updateUser(1L, new User("Updated1", "up1@test.com", 20));
        User update2 = service.updateUser(2L, new User("Updated2", "up2@test.com", 25));

        assertThat(update1.getId()).isEqualTo(1L);
        assertThat(update2.getId()).isEqualTo(2L);
    }
}
