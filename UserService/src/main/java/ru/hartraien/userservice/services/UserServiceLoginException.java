package ru.hartraien.userservice.services;

public class UserServiceLoginException extends Exception {
    public UserServiceLoginException() {
    }

    public UserServiceLoginException(String message) {
        super(message);
    }

    public UserServiceLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
