package com.example.restclientdemo.service;

import java.util.List;

import com.example.restclientdemo.client.UserClient;
import com.example.restclientdemo.model.User;

/**
 * Service demonstrating HTTP Interface usage from line 483-513 in spring-restclient-guide.adoc
 *
 * <p>Shows how to use declarative HTTP interfaces in a service. Note: Not annotated with @Service
 * to avoid bean conflicts. Used only for documentation examples.
 */
public class UserHttpInterfaceService {

    private final UserClient userClient;

    public UserHttpInterfaceService(UserClient userClient) {
        this.userClient = userClient;
    }

    public void demonstrateHttpInterface() {
        // Simple GET
        List<User> users = userClient.getAllUsers();

        // GET with path variable
        User user = userClient.getUser(1L);

        // POST
        User newUser = new User("John", "john@example.com", 30);
        User created = userClient.createUser(newUser);

        // PUT
        created.setEmail("john.doe@example.com");
        User updated = userClient.updateUser(created.getId(), created);

        // DELETE
        userClient.deleteUser(created.getId());

        // Search with query parameters
        List<User> results = userClient.searchUsers("John", 25);
    }

    // Public methods for testing - delegate to HTTP Interface client

    public List<User> getAllUsers() {
        return userClient.getAllUsers();
    }

    public User getUser(Long id) {
        return userClient.getUser(id);
    }

    public User createUser(User user) {
        return userClient.createUser(user);
    }

    public User updateUser(Long id, User user) {
        return userClient.updateUser(id, user);
    }

    public void deleteUser(Long id) {
        userClient.deleteUser(id);
    }

    public List<User> searchUsers(String name, Integer age) {
        return userClient.searchUsers(name, age);
    }
}
