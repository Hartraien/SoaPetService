package ru.hartraien.authservice.Exceptions;

public class UserServiceFailedInputException extends Exception {
    public UserServiceFailedInputException() {
    }

    public UserServiceFailedInputException(String message) {
        super(message);
    }

    public UserServiceFailedInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
