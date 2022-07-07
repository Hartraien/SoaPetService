package ru.hartraien.authservice.DTOs;

public class TokenResponse {

    private String accessToken;
    private String refreshToken;

    private String tokenHeader;

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public TokenResponse(String accessToken, String refreshToken, String tokenHeader) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenHeader = tokenHeader;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
