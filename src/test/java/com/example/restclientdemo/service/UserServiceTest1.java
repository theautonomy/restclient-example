package com.example.restclientdemo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.restclientdemo.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

/**
 * UserService test using @SpringBootTest to load Spring context.
 *
 * <p>This approach picks up Jackson 3 configuration automatically via:
 *
 * <ul>
 *   <li>application.properties: spring.http.converters.preferred-json-mapper=jackson
 *   <li>Jackson3Config bean that provides customized JsonMapper
 * </ul>
 *
 * <p>The auto-configured RestClient.Builder will use Jackson 3 message converters with
 * FAIL_ON_NULL_FOR_PRIMITIVES disabled to handle null → primitive mapping.
 */
@SpringBootTest
@DisplayName("UserService Test with @SpringBootTest (Jackson 3 via Spring Context)")
class UserServiceTest1 {

    @Autowired private RestClient.Builder restClientBuilder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        RestClient restClient =
                restClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
        userService = new UserService(restClient);
    }

    @Test
    @DisplayName("Should get single user using Jackson 2 from Spring context")
    void testGetUser() {
        User result = userService.getUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
        // age is 0 (primitive default) because JSONPlaceholder doesn't return it
        // Jackson 3 with FAIL_ON_NULL_FOR_PRIMITIVES disabled maps null to 0
        assertThat(result.getAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should get user with all JSONPlaceholder fields")
    void testGetUserWithAllFields() {
        User result = userService.getUser(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getUsername()).isEqualTo("Bret");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
        assertThat(result.getPhone()).isNotEmpty();
        assertThat(result.getWebsite()).isEqualTo("hildegard.org");
    }

    @Test
    @DisplayName("Should create user with Jackson 2 serialization")
    void testCreateUser() {
        User newUser = new User("Test User", "test@example.com", 25);
        User result = userService.createUser(newUser);

        // JSONPlaceholder simulates creation and returns ID 11
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L);
    }
}
