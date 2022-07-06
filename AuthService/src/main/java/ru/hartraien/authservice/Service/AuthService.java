package ru.hartraien.authservice.Service;

import ru.hartraien.authservice.DTOs.*;
import ru.hartraien.authservice.Exceptions.AuthConnectionException;
import ru.hartraien.authservice.Exceptions.AuthServiceException;
import ru.hartraien.authservice.Exceptions.AuthTokenInvalidException;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;

public interface AuthService {
    TokenResponse register(UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthConnectionException, AuthServiceException, UserServiceFailedInputException;

    TokenResponse login(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceFailedInputException, AuthServiceException, AuthConnectionException;

    TokenResponse refreshToken(TokenRequest tokenRequest) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException, AuthTokenInvalidException;

    void verifyToken(TokenRequest tokenRequest) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException, AuthTokenInvalidException;
}
