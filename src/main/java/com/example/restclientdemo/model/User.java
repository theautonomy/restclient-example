package com.example.restclientdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User model compatible with both our examples and JSONPlaceholder API.
 *
 * <p>JSONPlaceholder has additional fields (username, address, phone, website, company) which are
 * ignored. Our 'age' field is optional and not used by JSONPlaceholder.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String name;
    private String username; // JSONPlaceholder field
    private String email;
    private String phone; // JSONPlaceholder field
    private String website; // JSONPlaceholder field
    private Integer age; // Our custom field (not in JSONPlaceholder)

    public User() {}

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{id="
                + id
                + ", name='"
                + name
                + "', username='"
                + username
                + "', email='"
                + email
                + "', phone='"
                + phone
                + "', website='"
                + website
                + "', age="
                + age
                + "}";
    }
}
