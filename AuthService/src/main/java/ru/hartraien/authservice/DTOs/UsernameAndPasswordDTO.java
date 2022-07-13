package ru.hartraien.authservice.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UsernameAndPasswordDTO {

    @NotNull
    @NotBlank
    String username;
    @NotNull
    @NotBlank
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
