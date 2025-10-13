package com.example.restclientdemo.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import com.example.restclientdemo.exception.ClientErrorException;
import com.example.restclientdemo.exception.ServerErrorException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Custom error handler for RestClient that provides detailed error handling.
 *
 * <p>This handler demonstrates: - Distinguishing between client errors (4xx) and server errors
 * (5xx) - Extracting response body for error context - Throwing custom exceptions with meaningful
 * information - Logging error details for debugging
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    /**
     * Determines if the response has an error. Considers both 4xx (client errors) and 5xx (server
     * errors) as errors.
     *
     * @param response the HTTP response to check
     * @return true if the response status code indicates an error
     * @throws IOException if an I/O error occurs
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    /**
     * Handles errors by throwing custom exceptions based on the status code.
     *
     * <p>4xx errors are treated as client errors (bad request, invalid input, etc.) 5xx errors are
     * treated as server errors (service unavailable, internal error, etc.)
     *
     * @param url the URL of the request that failed
     * @param method the HTTP method of the request
     * @param response the HTTP response containing the error
     * @throws IOException if an I/O error occurs while reading the response
     */
    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response)
            throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        String responseBody = getResponseBody(response);

        System.err.println("Error occurred while calling: " + method + " " + url);
        System.err.println("Status Code: " + statusCode.value());
        System.err.println("Response Body: " + responseBody);

        if (statusCode.is4xxClientError()) {
            throw new ClientErrorException(
                    String.format(
                            "Client error: %s %s returned %d", method, url, statusCode.value()),
                    statusCode.value(),
                    responseBody);
        } else if (statusCode.is5xxServerError()) {
            throw new ServerErrorException(
                    String.format(
                            "Server error: %s %s returned %d", method, url, statusCode.value()),
                    statusCode.value(),
                    responseBody);
        }
    }

    /**
     * Extracts the response body as a string. Useful for including error details in exceptions.
     *
     * @param response the HTTP response
     * @return the response body as a string
     * @throws IOException if an I/O error occurs
     */
    private String getResponseBody(ClientHttpResponse response) throws IOException {
        try (BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
