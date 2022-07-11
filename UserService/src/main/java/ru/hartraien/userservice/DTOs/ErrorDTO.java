package ru.hartraien.userservice.DTOs;

public class ErrorDTO {
    private int statusCode;
    private String message;

    public ErrorDTO() {
    }

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
