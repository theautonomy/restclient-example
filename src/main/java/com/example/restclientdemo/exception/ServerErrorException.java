package com.example.restclientdemo.exception;

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
