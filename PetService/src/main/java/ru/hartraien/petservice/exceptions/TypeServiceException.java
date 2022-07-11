package ru.hartraien.petservice.exceptions;

public class TypeServiceException extends Exception {
    public TypeServiceException() {
    }

    public TypeServiceException(String message) {
        super(message);
    }

    public TypeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
