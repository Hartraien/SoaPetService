package ru.hartraien.userservice.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.hartraien.userservice.DTOs.NameValidationResponse;
import ru.hartraien.userservice.DTOs.UserServiceResponse;
import ru.hartraien.userservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.userservice.DTOs.UsernameDTO;
import ru.hartraien.userservice.entities.User;
import ru.hartraien.userservice.exceptions.UserServiceLoginException;
import ru.hartraien.userservice.services.UserService;

import java.time.LocalDateTime;
import java.util.Objects;


class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void init() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    private User generateUser(Long id, String username, String password, int failedLoginAttempts, boolean locked, LocalDateTime unlockTime) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setFailedLoginAttempts(failedLoginAttempts);
        user.setLocked(locked);
        user.setUnlockTime(unlockTime);
        return user;
    }

    private UserServiceResponse userToServiceResponse(User user) {
        UserServiceResponse response = new UserServiceResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return response;
    }

    private boolean compareUSR(UserServiceResponse expected, UserServiceResponse actual) {
        return expected != null && actual != null
                && Objects.equals(expected.getId(), actual.getId())
                && Objects.equals(expected.getUsername(), actual.getUsername());
    }

    @Test
    void login_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        Long id = 1L;
        User user = generateUser(id, username, password, 0, false, null);

        try {
            Mockito.when(userService.loginUser(username, password)).thenReturn(user);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }

        try {
            ResponseEntity<UserServiceResponse> loginRE = userController.login(usernameAndPasswordDTO);
            UserServiceResponse body = loginRE.getBody();
            Assertions.assertNotNull(body, "Body is null");
            UserServiceResponse expected = userToServiceResponse(user);
            String errorMessage = String.format("expected = {%d, %s}; actual = {%d, %s}", expected.getId(), expected.getUsername(), body.getId(), body.getUsername());
            Assertions.assertTrue(compareUSR(expected, body), errorMessage);
        } catch (UserServiceLoginException e) {
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
        String errorMessage = "Could not login user";

        try {
            Mockito.doThrow(new UserServiceLoginException(errorMessage)).when(userService).loginUser(username, password);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }

        try {
            userController.login(usernameAndPasswordDTO);
            Assertions.fail("Logged nonexistent user");
        } catch (UserServiceLoginException e) {
            Assertions.assertEquals(e.getMessage(), errorMessage);
        }

    }

    @Test
    void register_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        Long id = 1L;
        User user = generateUser(id, username, password, 0, false, null);

        try {
            Mockito.when(userService.register(username, password)).thenReturn(user);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }

        try {
            ResponseEntity<UserServiceResponse> loginRE = userController.register(usernameAndPasswordDTO);
            UserServiceResponse body = loginRE.getBody();
            Assertions.assertNotNull(body, "Body is null");
            UserServiceResponse expected = userToServiceResponse(user);
            String errorMessage = String.format("expected = {%d, %s}; actual = {%d, %s}", expected.getId(), expected.getUsername(), body.getId(), body.getUsername());
            Assertions.assertTrue(compareUSR(expected, body), errorMessage);
        } catch (UserServiceLoginException e) {
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
        String errorMessage = "Could not register user";

        try {
            Mockito.doThrow(new UserServiceLoginException(errorMessage)).when(userService).register(username, password);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }

        try {
            userController.register(usernameAndPasswordDTO);
            Assertions.fail("Logged nonexistent user");
        } catch (UserServiceLoginException e) {
            Assertions.assertEquals(e.getMessage(), errorMessage);
        }
    }

    @Test
    void getUserInfo_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);

        Long id = 1L;
        User user = generateUser(id, username, password, 0, false, null);

        try {
            Mockito.when(userService.getUserInfo(username)).thenReturn(user);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }

        try {
            ResponseEntity<UserServiceResponse> loginRE = userController.getUserInfo(usernameAndPasswordDTO);
            UserServiceResponse body = loginRE.getBody();
            Assertions.assertNotNull(body, "Body is null");
            UserServiceResponse expected = userToServiceResponse(user);
            String errorMessage = String.format("expected = {%d, %s}; actual = {%d, %s}", expected.getId(), expected.getUsername(), body.getId(), body.getUsername());
            Assertions.assertTrue(compareUSR(expected, body), errorMessage);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getUserInfo_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);
        String errorMessage = "No user with username " + username;


        try {
            Mockito.doThrow(new UserServiceLoginException(errorMessage)).when(userService).getUserInfo(username);
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }


        try {
            userController.getUserInfo(usernameAndPasswordDTO);
            Assertions.fail("Logged nonexistent user");
        } catch (UserServiceLoginException e) {
            Assertions.assertEquals(e.getMessage(), errorMessage);
        }
    }

    @Test
    void validateName_valid() {
        String username = "username";

        UsernameDTO usernameDTO = new UsernameDTO();
        usernameDTO.setUsername(username);

        Mockito.when(userService.checkIfUserExists(username)).thenReturn(false);

        ResponseEntity<NameValidationResponse> nameValidationResponseResponseEntity = userController.validateName(usernameDTO);
        NameValidationResponse body = nameValidationResponseResponseEntity.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.isValidName());
    }

    @Test
    void validateName_invalid() {
        String username = "username";

        UsernameDTO usernameDTO = new UsernameDTO();
        usernameDTO.setUsername(username);

        Mockito.when(userService.checkIfUserExists(username)).thenReturn(true);

        ResponseEntity<NameValidationResponse> nameValidationResponseResponseEntity = userController.validateName(usernameDTO);
        NameValidationResponse body = nameValidationResponseResponseEntity.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isValidName());
    }
}