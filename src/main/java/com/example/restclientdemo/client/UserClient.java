package com.example.restclientdemo.client;

import java.util.List;

import com.example.restclientdemo.model.User;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

/**
 * HTTP Interface example from line 424-448 in spring-restclient-guide.adoc
 *
 * <p>Declarative REST client interface using @HttpExchange annotations.
 */
@HttpExchange
public interface UserClient {

    @GetExchange("/users")
    List<User> getAllUsers();

    @GetExchange("/users/{id}")
    User getUser(@PathVariable Long id);

    @PostExchange("/users")
    User createUser(@RequestBody User user);

    @PutExchange("/users/{id}")
    User updateUser(@PathVariable Long id, @RequestBody User user);

    @DeleteExchange("/users/{id}")
    void deleteUser(@PathVariable Long id);

    @GetExchange("/users/search")
    List<User> searchUsers(@RequestParam String name, @RequestParam(required = false) Integer age);
}
