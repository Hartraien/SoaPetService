package ru.hartraien.userservice.DTOs;

public class NameValidationResponse {
    private boolean validName;

    public NameValidationResponse() {
    }

    public NameValidationResponse(boolean validName) {
        this.validName = validName;
    }

    public static NameValidationResponse validName() {
        return new NameValidationResponse(true);
    }

    public static NameValidationResponse invalidName() {
        return new NameValidationResponse(false);
    }

    public boolean isValidName() {
        return validName;
    }

    public void setValidName(boolean validName) {
        this.validName = validName;
    }
}
