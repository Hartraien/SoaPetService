package ru.hartraien.authservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hartraien.authservice.DTOs.*;
import ru.hartraien.authservice.Exceptions.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;

    private final UserServiceConnector userServiceConnector;

    @Value("${authorization.header-key}")
    private String authorizationHeader;

    @Autowired
    public AuthServiceImpl(JwtUtil jwtUtil, UserServiceConnector userServiceConnector) {
        this.jwtUtil = jwtUtil;
        this.userServiceConnector = userServiceConnector;
    }

    @Override
    public TokenResponse register(UsernameAndPasswordDTO usernameAndPasswordDTO) throws AuthConnectionException, AuthServiceException, UserServiceFailedInputException {
        try {
            UserServiceResponse userServiceResponse = userServiceConnector.register(usernameAndPasswordDTO);
            return generateTokenForUser(userServiceResponse);
        } catch (UserServiceException e) {
            throw new AuthServiceException("Could not parse answer", e);
        } catch (UserServiceConnectionException e) {
            throw new AuthConnectionException("Could not register user due to inner failure, try again later", e);
        }
    }

    @Override
    public TokenResponse login(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceFailedInputException, AuthServiceException, AuthConnectionException {
        try {
            UserServiceResponse userServiceResponse = userServiceConnector.loginUser(usernameAndPasswordDTO);
            return generateTokenForUser(userServiceResponse);
        } catch (UserServiceException e) {
            throw new AuthServiceException("Could not parse answer", e);
        } catch (UserServiceConnectionException e) {
            throw new AuthConnectionException("Could not register user due to inner failure, try again later", e);
        }
    }

    private TokenResponse generateTokenForUser(UserServiceResponse userServiceResponse) {
        long id = userServiceResponse.getId();
        String username = userServiceResponse.getUsername();
        String accessToken = jwtUtil.generateToken(id, username, "ACCESS");
        String refreshToken = jwtUtil.generateToken(id, username, "REFRESH");
        return new TokenResponse(accessToken, refreshToken, authorizationHeader);
    }

    @Override
    public TokenResponse refreshToken(TokenRequest tokenRequest) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException, AuthTokenInvalidException {
        UserServiceResponse userServiceResponse = getUserServiceResponse(tokenRequest);
        return generateTokenForUser(userServiceResponse);
    }

    @Override
    public UserServiceResponse verifyToken(TokenRequest tokenRequest) throws AuthServiceException, AuthConnectionException, UserServiceFailedInputException, AuthTokenInvalidException {
        return getUserServiceResponse(tokenRequest);
    }

    private UserServiceResponse getUserServiceResponse(TokenRequest tokenRequest) throws UserServiceFailedInputException, AuthServiceException, AuthConnectionException, AuthTokenInvalidException {
        String token = tokenRequest.getToken();
        if(jwtUtil.validateToken(token)){
        long id = jwtUtil.getIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        try {
            return userServiceConnector.checkUserByUsernameAndId(id, username);
        } catch (UserServiceException e) {
            throw new AuthServiceException("Could not parse answer", e);
        } catch (UserServiceConnectionException e) {
            throw new AuthConnectionException("Could not register user due to inner failure, try again later", e);
        }}
        else
            throw new AuthTokenInvalidException("Invalid Token");
    }
}
