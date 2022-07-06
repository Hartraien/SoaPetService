package ru.hartraien.authservice.Exceptions;

import ru.hartraien.authservice.Exceptions.UserServiceException;

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
