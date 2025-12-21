package com.example.restclientdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Jackson 3 configuration for Spring Boot 4.
 *
 * <p>Jackson 3 (tools.jackson package) is the default in Spring Boot 4. This configuration
 * customizes the JsonMapper to handle null values for primitive types gracefully.
 *
 * <p>Key differences from Jackson 2:
 *
 * <ul>
 *   <li>Uses immutable JsonMapper instead of mutable ObjectMapper
 *   <li>Package changed from com.fasterxml.jackson to tools.jackson
 *   <li>Builder pattern: JsonMapper.builder()...build()
 * </ul>
 *
 * <p>By default, Jackson 3 has FAIL_ON_NULL_FOR_PRIMITIVES enabled, which causes deserialization to
 * fail when a JSON null is mapped to a primitive type (int, long, boolean, etc.). This
 * configuration disables that behavior, allowing null to map to default values (0, false, etc.).
 *
 * @see <a href="https://www.danvega.dev/blog/2025/11/10/jackson-3-spring-boot-4">Jackson 3 in
 *     Spring Boot 4</a>
 */
@Configuration
public class Jackson3Config {

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                // Allow null values to be mapped to primitive defaults (0, false, etc.)
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                // Ignore unknown properties in JSON (like @JsonIgnoreProperties(ignoreUnknown =
                // true))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }
}
