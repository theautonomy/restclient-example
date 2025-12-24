package com.example.restclientdemo.util;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Utility to list all HTTP message converters registered by Spring.
 *
 * <p>This component runs at startup and prints all message converters available in the RestClient,
 * showing what's configured for JSON, XML, etc.
 */
@Component
public class MessageConverterInfo implements CommandLineRunner {

    private final RestClient.Builder restClientBuilder;

    public MessageConverterInfo(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("HTTP Message Converters (from auto-configured RestClient.Builder)");
        System.out.println("=".repeat(60) + "\n");

        // Capture converters from RestClient.Builder using configureMessageConverters
        List<HttpMessageConverter<?>> converters = new java.util.ArrayList<>();
        restClientBuilder
                .configureMessageConverters(
                        builder -> {
                            var result = builder.registerDefaults().build();
                            result.forEach(converters::add);
                        })
                .build();

        int index = 1;
        for (HttpMessageConverter<?> converter : converters) {
            String className = converter.getClass().getName();
            String simpleName = converter.getClass().getSimpleName();

            // Determine the type (Jackson 2, Jackson 3, etc.)
            String type = getConverterType(className);

            System.out.printf("%2d. %-45s %s%n", index++, simpleName, type);

            // Print supported media types
            converter
                    .getSupportedMediaTypes()
                    .forEach(mediaType -> System.out.printf("      - %s%n", mediaType));
        }

        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    private String getConverterType(String className) {
        if (className.contains("JacksonJson")) {
            return "[Jackson 3 - tools.jackson]";
        } else if (className.contains("MappingJackson2")) {
            return "[Jackson 2 - com.fasterxml.jackson]";
        } else if (className.contains("Jaxb2")) {
            return "[JAXB XML]";
        } else if (className.contains("String")) {
            return "[String/Text]";
        } else if (className.contains("ByteArray")) {
            return "[Binary]";
        } else if (className.contains("Resource")) {
            return "[Resource]";
        } else if (className.contains("Form")) {
            return "[Form Data]";
        }
        return "";
    }
}
