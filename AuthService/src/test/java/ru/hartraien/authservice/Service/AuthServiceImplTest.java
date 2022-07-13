package ru.hartraien.authservice.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.hartraien.authservice.DTOs.TokenRequest;
import ru.hartraien.authservice.DTOs.TokenResponse;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.AuthTokenInvalidException;
import ru.hartraien.authservice.Exceptions.UserServiceException;
import ru.hartraien.authservice.Utility.TokenTypes;

class AuthServiceImplTest {

    private JwtUtil jwtUtil;
    private AuthService authService;
    private UserServiceConnector userServiceConnector;

    @BeforeEach
    void init() {
        jwtUtil = new JwtUtilImpl(86400, "id");
        userServiceConnector = Mockito.mock(UserServiceConnector.class);
        authService = new AuthServiceImpl(jwtUtil, userServiceConnector);
    }

    @Test
    void register_Valid() {
        String username = "username";
        String password = "password";

        long id = 1L;
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(id);
        userServiceResponse.setUsername(username);


        try {
            Mockito.when(userServiceConnector.register(usernameAndPasswordDTO)).thenReturn(userServiceResponse);
            TokenResponse register = authService.register(usernameAndPasswordDTO);
            Assertions.assertEquals(username, jwtUtil.getUsernameFromToken(register.getAccessToken()));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void register_Invalid() {
        String username = "username";
        String password = "password";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);


        try {
            Mockito.doThrow(new UserServiceException("Wrong input")).when(userServiceConnector).register(Mockito.any());
            TokenResponse register = authService.register(usernameAndPasswordDTO);
            Assertions.fail("Registered failure");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof UserServiceException);
        }
    }

    @Test
    void login_Valid() {
        String username = "username";
        String password = "password";

        long id = 1L;
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(id);
        userServiceResponse.setUsername(username);


        try {
            Mockito.when(userServiceConnector.loginUser(usernameAndPasswordDTO)).thenReturn(userServiceResponse);
            TokenResponse register = authService.login(usernameAndPasswordDTO);
            Assertions.assertEquals(username, jwtUtil.getUsernameFromToken(register.getAccessToken()));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void login_Invalid() {
        String username = "username";
        String password = "password";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);


        try {
            Mockito.doThrow(new UserServiceException("Wrong input")).when(userServiceConnector).loginUser(Mockito.any());
            TokenResponse register = authService.login(usernameAndPasswordDTO);
            Assertions.fail("Login failure");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof UserServiceException);
        }
    }

    @Test
    void refreshToken_Valid() {
        long id = 1L;
        String username = "username";
        String token = jwtUtil.generateToken(id, username, TokenTypes.ACCESS);

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(token);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(id);
        userServiceResponse.setUsername(username);

        try {
            Mockito.when(userServiceConnector.checkUserByUsernameAndId(id, username)).thenReturn(userServiceResponse);
            TokenResponse tokenResponse = authService.refreshToken(tokenRequest);
            Assertions.assertEquals(jwtUtil.getIdFromToken(token), jwtUtil.getIdFromToken(tokenResponse.getAccessToken()));
            Assertions.assertEquals(jwtUtil.getUsernameFromToken(token), jwtUtil.getUsernameFromToken(tokenResponse.getAccessToken()));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void refreshToken_Invalid() {
        long id = 1L;
        String username = "username";
        String token = "jwtUtil.generateToken(id, username, TokenTypes.ACCESS);";

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(token);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(id);
        userServiceResponse.setUsername(username);

        try {
            Mockito.when(userServiceConnector.checkUserByUsernameAndId(id, username)).thenReturn(userServiceResponse);
            TokenResponse tokenResponse = authService.refreshToken(tokenRequest);
            Assertions.fail("Parsed wrong token");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthTokenInvalidException);
        }
    }

    @Test
    void verifyToken_Valid() {
        long id = 1L;
        String username = "username";
        String token = jwtUtil.generateToken(id, username, TokenTypes.ACCESS);

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(token);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(id);
        userServiceResponse.setUsername(username);

        try {
            Mockito.when(userServiceConnector.checkUserByUsernameAndId(id, username)).thenReturn(userServiceResponse);
            UserServiceResponse userServiceResponse1 = authService.verifyToken(tokenRequest);
            Assertions.assertEquals(userServiceResponse.getId(), userServiceResponse1.getId());
            Assertions.assertEquals(userServiceResponse.getUsername(), userServiceResponse1.getUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}