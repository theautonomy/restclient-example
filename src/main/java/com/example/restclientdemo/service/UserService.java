package com.example.restclientdemo.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.restclientdemo.exception.ServiceUnavailableException;
import com.example.restclientdemo.exception.UserNotFoundException;
import com.example.restclientdemo.model.User;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * Service demonstrating all RestClient patterns from the AsciiDoc guide.
 *
 * <p>This service contains working examples of all code snippets shown in the
 * spring-restclient-guide.adoc documentation. Note: Not annotated with @Service to avoid bean
 * conflicts. Created manually in DemoRunner.
 */
public class UserService {

    private final RestClient restClient;

    public UserService(RestClient restClient) {
        this.restClient = restClient;
    }

    // ===== Basic Operations =====

    /** Simple GET request example from line 119-124 */
    public User getUser(Long id) {
        return restClient.get().uri("/users/{id}", id).retrieve().body(User.class);
    }

    /** Get all users example from line 126-132 */
    public List<User> getAllUsers() {
        return restClient
                .get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});
    }

    /** GET with query parameters using UriBuilder (line 143-152) */
    public List<User> searchUsers(String name, Integer minAge) {
        return restClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/users/search")
                                        .queryParam("name", name)
                                        .queryParam("minAge", minAge)
                                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});
    }

    /** GET with query parameters using string formatting (line 159-164) */
    public List<User> searchUsersWithStringFormat(String name, Integer age) {
        return restClient
                .get()
                .uri("/users/search?name={name}&age={age}", name, age)
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});
    }

    /** POST request example (line 173-180) */
    public User createUser(User user) {
        return restClient
                .post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .body(User.class);
    }

    /** Form data submission example (line 187-198) */
    public User submitForm(String name, String email) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", name);
        formData.add("email", email);

        return restClient
                .post()
                .uri("/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(User.class);
    }

    /** PUT request example (line 207-214) */
    public User updateUser(Long id, User user) {
        return restClient
                .put()
                .uri("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .body(User.class);
    }

    /** PATCH request example (line 216-223) */
    public User partialUpdateUser(Long id, Map<String, Object> updates) {
        return restClient
                .patch()
                .uri("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updates)
                .retrieve()
                .body(User.class);
    }

    /** DELETE request example (line 232-237) */
    public void deleteUser(Long id) {
        restClient.delete().uri("/users/{id}", id).retrieve().toBodilessEntity();
    }

    /** DELETE with status code return (line 239-246) */
    public int deleteUserWithStatus(Long id) {
        ResponseEntity<Void> response =
                restClient.delete().uri("/users/{id}", id).retrieve().toBodilessEntity();

        return response.getStatusCode().value();
    }

    // ===== Advanced Features =====

    /** Custom headers example (line 257-264) */
    public User getUserWithHeaders(Long id) {
        return restClient
                .get()
                .uri("/users/{id}", id)
                .header("X-Request-ID", UUID.randomUUID().toString())
                .header("X-API-Version", "v2")
                .retrieve()
                .body(User.class);
    }

    /** Headers consumer example (line 271-281) */
    public User getUserWithDynamicHeaders(Long id) {
        return restClient
                .get()
                .uri("/users/{id}", id)
                .headers(
                        headers -> {
                            headers.set("X-Request-ID", UUID.randomUUID().toString());
                            headers.set("X-Timestamp", Instant.now().toString());
                            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                        })
                .retrieve()
                .body(User.class);
    }

    /** Basic authentication example (line 290-296) */
    public User authenticatedRequest(String username, String password) {
        return restClient
                .get()
                .uri("/secure/user")
                .headers(headers -> headers.setBasicAuth(username, password))
                .retrieve()
                .body(User.class);
    }

    /** Response entity access example (line 322-327) */
    public ResponseEntity<User> getUserWithMetadata(Long id) {
        return restClient.get().uri("/users/{id}", id).retrieve().toEntity(User.class);
    }

    /** Process response with metadata (line 329-338) */
    public void processUserResponse(Long id) {
        ResponseEntity<User> response =
                restClient.get().uri("/users/{id}", id).retrieve().toEntity(User.class);

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body: " + response.getBody());
    }

    // ===== Error Handling =====

    /** Basic error handling example (line 347-358) */
    public User getUserWithErrorHandling(Long id) {
        try {
            return restClient.get().uri("/users/{id}", id).retrieve().body(User.class);
        } catch (RestClientResponseException e) {
            System.err.println("Error status: " + e.getStatusCode());
            System.err.println("Error body: " + e.getResponseBodyAsString());
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    /** Custom error handling with status handlers (line 365-378) */
    public User getUserWithCustomErrorHandling(Long id) {
        return restClient
                .get()
                .uri("/users/{id}", id)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new UserNotFoundException(
                                    "User not found: " + response.getStatusCode());
                        })
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        (request, response) -> {
                            throw new ServiceUnavailableException(
                                    "Service error: " + response.getStatusCode());
                        })
                .body(User.class);
    }

    /** Error handling with Optional (line 779-789) */
    public Optional<User> getUserSafely(Long id) {
        try {
            User user = restClient.get().uri("/users/{id}", id).retrieve().body(User.class);
            return Optional.ofNullable(user);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}
