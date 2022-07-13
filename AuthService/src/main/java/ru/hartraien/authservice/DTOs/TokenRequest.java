package ru.hartraien.authservice.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TokenRequest {
    @NotNull
    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
