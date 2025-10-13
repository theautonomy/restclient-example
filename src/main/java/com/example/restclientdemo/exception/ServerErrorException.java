package com.example.restclientdemo.exception;

/**
 * Exception thrown for HTTP 5xx server errors.
 *
 * <p>This exception is raised when the server returns a server error status code, indicating issues
 * such as: - 500 Internal Server Error: Unexpected server error - 502 Bad Gateway: Invalid response
 * from upstream server - 503 Service Unavailable: Server temporarily unavailable - 504 Gateway
 * Timeout: Upstream server timeout
 */
public class ServerErrorException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public ServerErrorException(String message, int statusCode, String responseBody) {
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
        return "ServerErrorException{"
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
