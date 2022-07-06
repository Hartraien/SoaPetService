package ru.hartraien.authservice.Exceptions;

public class AuthConnectionException extends Exception {
    public AuthConnectionException() {
    }

    public AuthConnectionException(String message) {
        super(message);
    }

    public AuthConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
