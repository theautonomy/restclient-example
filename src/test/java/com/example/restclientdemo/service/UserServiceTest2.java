package com.example.restclientdemo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.restclientdemo.model.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

/**
 * UserService test with manually configured Jackson 2 message converters.
 *
 * <p>This approach demonstrates how to explicitly configure RestClient to use Jackson 2 without
 * relying on Spring Boot auto-configuration.
 *
 * <p>Key points:
 *
 * <ul>
 *   <li>Creates Jackson 2 ObjectMapper (com.fasterxml.jackson.databind.ObjectMapper)
 *   <li>Configures MappingJackson2HttpMessageConverter with the ObjectMapper
 *   <li>Sets the converter on RestClient.Builder via messageConverters()
 * </ul>
 */
@DisplayName("UserService Test with Manual Jackson 2 Configuration")
@SuppressWarnings("removal") // Jackson 2 is deprecated in Spring Boot 4 but still supported
class UserServiceTest2 {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Create Jackson 2 ObjectMapper with custom configuration
        ObjectMapper jackson2ObjectMapper = new ObjectMapper();
        jackson2ObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Create Jackson 2 message converter
        MappingJackson2HttpMessageConverter jackson2Converter =
                new MappingJackson2HttpMessageConverter(jackson2ObjectMapper);

        // Build RestClient with Jackson 2 converter (replaces default Jackson 3)
        RestClient restClient =
                RestClient.builder()
                        .baseUrl("https://jsonplaceholder.typicode.com")
                        .configureMessageConverters(
                                builder ->
                                        builder.registerDefaults()
                                                .withJsonConverter(jackson2Converter))
                        .build();

        userService = new UserService(restClient);
    }

    @Test
    @DisplayName("Should get single user using manually configured Jackson 2")
    void testGetUser() {
        User result = userService.getUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Leanne Graham");
        assertThat(result.getEmail()).isEqualTo("Sincere@april.biz");
        // age is 0 (primitive default) because JSONPlaceholder doesn't return it
        // Jackson 2 uses setters and maps null to 0 for primitive int
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
    @DisplayName("Should get all users using Jackson 2")
    void testGetAllUsers() {
        List<User> result = userService.getAllUsers();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(10);
        assertThat(result.get(0).getName()).isEqualTo("Leanne Graham");
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
