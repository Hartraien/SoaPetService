package ru.hartraien.authservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.hartraien.authservice.DTOs.TokenRequest;
import ru.hartraien.authservice.DTOs.TokenResponse;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.AuthConnectionException;
import ru.hartraien.authservice.Exceptions.AuthServiceException;
import ru.hartraien.authservice.Exceptions.AuthTokenInvalidException;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;
import ru.hartraien.authservice.Service.AuthService;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException {
        return ResponseEntity.ok(authService.register(usernameAndPasswordDTO));
    }

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException {
        return ResponseEntity.ok(authService.login(usernameAndPasswordDTO));
    }

    @PostMapping("refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody TokenRequest tokenRequest) throws AuthServiceException, AuthTokenInvalidException, AuthConnectionException, UserServiceFailedInputException {
        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }

    @PostMapping("verifyToken")
    public ResponseEntity<UserServiceResponse> verifyToken(@Valid @RequestBody TokenRequest tokenRequest) throws AuthServiceException, AuthTokenInvalidException, AuthConnectionException, UserServiceFailedInputException {
        UserServiceResponse userServiceResponse = authService.verifyToken(tokenRequest);
        return ResponseEntity.ok(userServiceResponse);
    }


}
