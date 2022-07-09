package ru.hartraien.authservice.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hartraien.authservice.DTOs.TokenRequest;
import ru.hartraien.authservice.DTOs.TokenResponse;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.*;
import ru.hartraien.authservice.Utility.TokenTypes;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;

    private final UserServiceConnector userServiceConnector;

    @Value("${authorization.header-key}")
    private String authorizationHeader;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

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
        logger.debug("Id for token = " + id + " Username for token = \"" + username + "\"");
        String accessToken = jwtUtil.generateToken(id, username, TokenTypes.ACCESS);
        String refreshToken = jwtUtil.generateToken(id, username, TokenTypes.REFRESH);
        logger.debug("access token = " + accessToken);
        logger.debug("access token username = " + jwtUtil.getUsernameFromToken(accessToken));
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
        if (jwtUtil.validateToken(token)) {
            long id = jwtUtil.getIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            logger.debug("Id from token = " + id + " Username from token = \"" + username + "\"");
            try {
                return userServiceConnector.checkUserByUsernameAndId(id, username);
            } catch (UserServiceException e) {
                logger.debug("Could not parse answer due to " + e.getMessage(), e);
                throw new AuthServiceException("Could not parse answer", e);
            } catch (UserServiceConnectionException e) {
                logger.debug("Could not register user due to inner failure, try again later due to " + e.getMessage(), e);
                throw new AuthConnectionException("Could not register user due to inner failure, try again later", e);
            }
        } else{
            logger.debug("Invalid Token");
            throw new AuthTokenInvalidException("Invalid Token");
        }
    }
}
