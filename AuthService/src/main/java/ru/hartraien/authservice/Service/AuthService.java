package ru.hartraien.authservice.Service;

import ru.hartraien.authservice.DTOs.TokenRequest;
import ru.hartraien.authservice.DTOs.TokenResponse;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.AuthConnectionException;
import ru.hartraien.authservice.Exceptions.AuthServiceException;
import ru.hartraien.authservice.Exceptions.AuthTokenInvalidException;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;

public interface AuthService {
    TokenResponse register(UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthConnectionException, AuthServiceException, UserServiceFailedInputException;

    TokenResponse login(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceFailedInputException, AuthServiceException, AuthConnectionException;

    TokenResponse refreshToken(TokenRequest tokenRequest) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException, AuthTokenInvalidException;

    UserServiceResponse verifyToken(TokenRequest tokenRequest) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException, AuthTokenInvalidException;
}
