package ru.hartraien.petservice.exceptions;

public class PetServiceException extends Exception {
    private String outerMessage;

    public PetServiceException() {
    }

    public PetServiceException(String message) {
        super(message);
    }

    public PetServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetServiceException(String innerMessage, String outerMessage) {
        super(innerMessage);
        this.outerMessage = outerMessage;
    }

    public String getOuterMessage() {
        return outerMessage;
    }
}
