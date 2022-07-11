package ru.hartraien.gatewayservice.exceptions;

public class AuthServiceException extends Exception {
    public AuthServiceException() {
    }

    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
