package ru.hartraien.authservice.Exceptions;

public class AuthTokenExpiredException extends Exception {
    public AuthTokenExpiredException() {
    }

    public AuthTokenExpiredException(String message) {
        super(message);
    }

    public AuthTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
