package ru.hartraien.userservice.exceptions;

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
