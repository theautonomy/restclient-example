package com.example.restclientdemo.exception;

/**
 * Exception thrown for HTTP 4xx client errors.
 *
 * <p>This exception is raised when the server returns a client error status code, indicating issues
 * such as: - 400 Bad Request: Invalid request syntax or parameters - 401 Unauthorized:
 * Authentication required - 403 Forbidden: Access denied - 404 Not Found: Resource not found - 422
 * Unprocessable Entity: Validation errors
 */
public class ClientErrorException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public ClientErrorException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "ClientErrorException{"
                + "statusCode="
                + statusCode
                + ", message='"
                + getMessage()
                + '\''
                + ", responseBody='"
                + responseBody
                + '\''
                + '}';
    }
}
