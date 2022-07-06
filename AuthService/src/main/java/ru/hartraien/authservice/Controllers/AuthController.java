package ru.hartraien.authservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.hartraien.authservice.DTOs.*;
import ru.hartraien.authservice.Exceptions.AuthConnectionException;
import ru.hartraien.authservice.Exceptions.AuthServiceException;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;
import ru.hartraien.authservice.Service.AuthService;
import ru.hartraien.authservice.Service.AuthTokenException;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<TokenResponse> register(@RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException {
        return ResponseEntity.ok(authService.register(usernameAndPasswordDTO));
    }

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException {
        return ResponseEntity.ok(authService.login(usernameAndPasswordDTO));
    }

    @GetMapping("refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRequest tokenRequest) throws AuthServiceException, AuthTokenException, AuthConnectionException, UserServiceFailedInputException {
        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }

    @PostMapping("verifyToken")
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequest tokenRequest) throws AuthServiceException, AuthTokenException, AuthConnectionException, UserServiceFailedInputException {
        authService.verifyToken(tokenRequest);
        return ResponseEntity.ok().build();
    }


}