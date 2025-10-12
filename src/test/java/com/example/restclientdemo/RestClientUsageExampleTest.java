package com.example.restclientdemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class RestClientUsageExampleTest {

    private RestClientUsageExample examples;

    @BeforeEach
    void setUp() {
        examples = new RestClientUsageExample();
    }

    @Test
    @DisplayName("Demo 1: Basic RestClient should be created successfully")
    void testDemo1_BasicRestClient() {
        // When
        RestClient basicClient = examples.createBasicRestClient();

        // Then
        assertThat(basicClient).isNotNull();
        assertThat(basicClient).isInstanceOf(RestClient.class);
    }

    @Test
    @DisplayName("Demo 2: Advanced RestClient with Consumer (production + auth)")
    void testDemo2_AdvancedRestClientWithProductionAndAuth() {
        // When
        RestClient advancedClient = examples.createAdvancedRestClient("production", true);

        // Then
        assertThat(advancedClient).isNotNull();
        assertThat(advancedClient).isInstanceOf(RestClient.class);
    }

    @Test
    @DisplayName("Demo 2: Advanced RestClient with Consumer (dev + no auth)")
    void testDemo2_AdvancedRestClientWithDevAndNoAuth() {
        // When
        RestClient advancedClient = examples.createAdvancedRestClient("dev", false);

        // Then
        assertThat(advancedClient).isNotNull();
        assertThat(advancedClient).isInstanceOf(RestClient.class);
    }

    @Test
    @DisplayName("Demo 3: Step by Step Configuration should build RestClient")
    void testDemo3_StepByStepClient() {
        // When
        RestClient stepByStepClient = examples.createStepByStepClient();

        // Then
        assertThat(stepByStepClient).isNotNull();
        assertThat(stepByStepClient).isInstanceOf(RestClient.class);
    }

    @Test
    @DisplayName("Demo 4: Header Inspection should execute without errors")
    void testDemo4_HeaderInspection() {
        // When/Then - Should not throw any exceptions
        assertDoesNotThrow(() -> examples.demonstrateHeaderInspection());
    }

    @Test
    @DisplayName("Demo 5: Multiple API Clients should be created successfully")
    void testDemo5_MultipleApiClients() {
        // When
        RestClientUsageExample.ApiClients apiClients = examples.new ApiClients();

        // Then
        assertThat(apiClients).isNotNull();
    }

    @Test
    @DisplayName("All demos should execute successfully in sequence")
    void testAllDemosInSequence() {
        // Simulate the main method flow
        assertDoesNotThrow(
                () -> {
                    // Demo 1: Basic RestClient
                    RestClient basicClient = examples.createBasicRestClient();
                    assertThat(basicClient).isNotNull();

                    // Demo 2: Advanced RestClient with Consumer
                    RestClient advancedClient =
                            examples.createAdvancedRestClient("production", true);
                    assertThat(advancedClient).isNotNull();

                    // Demo 3: Step by Step Configuration
                    RestClient stepByStepClient = examples.createStepByStepClient();
                    assertThat(stepByStepClient).isNotNull();

                    // Demo 4: Header Inspection
                    examples.demonstrateHeaderInspection();

                    // Demo 5: Multiple API Clients
                    RestClientUsageExample.ApiClients apiClients = examples.new ApiClients();
                    assertThat(apiClients).isNotNull();
                });
    }

    @Test
    @DisplayName("RestClient configurations should produce different instances")
    void testDifferentRestClientInstances() {
        // When
        RestClient basicClient = examples.createBasicRestClient();
        RestClient advancedClient = examples.createAdvancedRestClient("production", true);
        RestClient stepByStepClient = examples.createStepByStepClient();

        // Then
        assertThat(basicClient).isNotSameAs(advancedClient);
        assertThat(basicClient).isNotSameAs(stepByStepClient);
        assertThat(advancedClient).isNotSameAs(stepByStepClient);
    }

    @Test
    @DisplayName("ApiClients should support multiple independent clients")
    void testApiClientsIndependence() {
        // When
        RestClientUsageExample.ApiClients apiClients1 = examples.new ApiClients();
        RestClientUsageExample.ApiClients apiClients2 = examples.new ApiClients();

        // Then
        assertThat(apiClients1).isNotNull();
        assertThat(apiClients2).isNotNull();
        assertThat(apiClients1).isNotSameAs(apiClients2);
    }
}
