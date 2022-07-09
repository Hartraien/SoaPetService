package ru.hartraien.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.hartraien.userservice.DTOs.UserServiceResponse;
import ru.hartraien.userservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.userservice.entities.User;
import ru.hartraien.userservice.exceptions.UserServiceLoginException;
import ru.hartraien.userservice.services.UserService;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<UserServiceResponse> login(@RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceLoginException {
        User user = userService.loginUser(usernameAndPasswordDTO.getUsername(), usernameAndPasswordDTO.getPassword());
        UserServiceResponse userServiceResponse = convertToResponse(user);
        return ResponseEntity.ok(userServiceResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserServiceResponse> register(@RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceLoginException {
        User user = userService.register(usernameAndPasswordDTO.getUsername(), usernameAndPasswordDTO.getPassword());
        UserServiceResponse userServiceResponse = convertToResponse(user);
        return ResponseEntity.ok(userServiceResponse);
    }

    @PostMapping("/getUserInfo")
    public ResponseEntity<UserServiceResponse> getUserInfo(@RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceLoginException {
        User user = userService.getUserInfo(usernameAndPasswordDTO.getUsername());
        UserServiceResponse userServiceResponse = convertToResponse(user);
        return ResponseEntity.ok(userServiceResponse);
    }

    @PostMapping("/validName")
    public ResponseEntity<NameValidationResponse> validateName(@RequestBody UsernameDTO usernameDTO) {
        NameValidationResponse nameValidationResponse;
        if (userService.checkIfUserExists(usernameDTO.getUsername())) {
            nameValidationResponse = NameValidationResponse.invalidName();
        } else {
            nameValidationResponse = NameValidationResponse.validName();
        }
        return ResponseEntity.ok(nameValidationResponse);
    }

    private UserServiceResponse convertToResponse(User user) {
        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(user.getId());
        userServiceResponse.setUsername(user.getUsername());
        return userServiceResponse;
    }
}
