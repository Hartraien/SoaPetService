package ru.hartraien.authservice.Exceptions;

public class AuthTokenInvalidException extends Exception {
    public AuthTokenInvalidException() {
    }

    public AuthTokenInvalidException(String message) {
        super(message);
    }

    public AuthTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
