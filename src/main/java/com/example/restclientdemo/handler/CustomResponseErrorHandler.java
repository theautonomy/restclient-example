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

/** Custom error handler for RestClient that provides detailed error handling. */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

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

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        try (BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
