package ru.hartraien.authservice.Service;

public class AuthTokenException extends Exception {
    public AuthTokenException() {
    }

    public AuthTokenException(String message) {
        super(message);
    }

    public AuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
