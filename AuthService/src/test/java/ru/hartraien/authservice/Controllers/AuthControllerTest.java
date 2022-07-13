package ru.hartraien.authservice.Controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.hartraien.authservice.DTOs.TokenRequest;
import ru.hartraien.authservice.DTOs.TokenResponse;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.AuthServiceException;
import ru.hartraien.authservice.Service.AuthService;

class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void init() {
        authService = Mockito.mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void register_Valid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        String username = "username";
        String password = "password";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        try {
            Mockito.when(authService.register(usernameAndPasswordDTO)).thenReturn(tokenResponse);
            ResponseEntity<TokenResponse> register = authController.register(usernameAndPasswordDTO);
            TokenResponse response = register.getBody();

            Assertions.assertEquals(tokenResponse.getAccessToken(), response.getAccessToken());
            Assertions.assertEquals(tokenResponse.getRefreshToken(), response.getRefreshToken());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void register_Invalid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        String username = "username";
        String password = "password";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        String errorMessage = "Error message";
        try {
            Mockito.doThrow(new AuthServiceException(errorMessage)).when(authService).register(usernameAndPasswordDTO);
            Assertions.assertThrows(AuthServiceException.class, () -> authController.register(usernameAndPasswordDTO));
        } catch (Exception e) {
            Assertions.fail("Thrown exception during mock");
        }
    }

    @Test
    void login_Valid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        String username = "username";
        String password = "password";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        try {
            Mockito.when(authService.login(usernameAndPasswordDTO)).thenReturn(tokenResponse);
            ResponseEntity<TokenResponse> register = authController.login(usernameAndPasswordDTO);
            TokenResponse response = register.getBody();

            Assertions.assertEquals(tokenResponse.getAccessToken(), response.getAccessToken());
            Assertions.assertEquals(tokenResponse.getRefreshToken(), response.getRefreshToken());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void login_Invalid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        String username = "username";
        String password = "password";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        String errorMessage = "Error message";
        try {
            Mockito.doThrow(new AuthServiceException(errorMessage)).when(authService).login(usernameAndPasswordDTO);
            Assertions.assertThrows(AuthServiceException.class, () -> authController.login(usernameAndPasswordDTO));
        } catch (Exception e) {
            Assertions.fail("Thrown exception during mock");
        }
    }

    @Test
    void refreshToken_Valid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(accessToken);

        try {
            Mockito.when(authService.refreshToken(tokenRequest)).thenReturn(tokenResponse);
            ResponseEntity<TokenResponse> tokenResponseResponseEntity = authController.refreshToken(tokenRequest);
            TokenResponse body = tokenResponseResponseEntity.getBody();
            Assertions.assertEquals(tokenResponse.getAccessToken(), body.getAccessToken());
            Assertions.assertEquals(tokenResponse.getRefreshToken(), body.getRefreshToken());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void refreshToken_Invalid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(accessToken);

        String errorMessage = "Error message";

        try {
            Mockito.doThrow(new AuthServiceException(errorMessage)).when(authService).refreshToken(tokenRequest);
            Assertions.assertThrows(AuthServiceException.class, () -> authController.refreshToken(tokenRequest));
        } catch (Exception e) {
            Assertions.fail("Thrown exception during mock");
        }
    }

    @Test
    void verifyToken_Valid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(accessToken);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1L);
        userServiceResponse.setUsername("username");

        try {
            Mockito.when(authService.verifyToken(tokenRequest)).thenReturn(userServiceResponse);
            ResponseEntity<UserServiceResponse> userServiceResponseResponseEntity = authController.verifyToken(tokenRequest);
            UserServiceResponse body = userServiceResponseResponseEntity.getBody();
            Assertions.assertEquals(userServiceResponse.getId(), body.getId());
            Assertions.assertEquals(userServiceResponse.getUsername(), body.getUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void verifyToken_Invalid() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, "Authorization");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(accessToken);

        String errorMessage = "Error message";

        try {
            Mockito.doThrow(new AuthServiceException(errorMessage)).when(authService).verifyToken(tokenRequest);
            Assertions.assertThrows(AuthServiceException.class, () -> authController.verifyToken(tokenRequest));
        } catch (Exception e) {
            Assertions.fail("Thrown exception during mock");
        }
    }
}