package ru.hartraien.authservice.Exceptions;

public class UserServiceConnectionException extends Exception {
    public UserServiceConnectionException() {
    }

    public UserServiceConnectionException(String message) {
        super(message);
    }

    public UserServiceConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
