package ru.hartraien.authservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import ru.hartraien.authservice.DTOs.UserServiceErrorDTO;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.UserServiceConnectionException;
import ru.hartraien.authservice.Exceptions.UserServiceException;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;

@Service
public class UserServiceConnectorImpl implements UserServiceConnector {

    private final RestTemplate restTemplate;

    private final String userServiceAddress;

    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceConnectorImpl.class);

    @Autowired
    public UserServiceConnectorImpl(RestTemplate restTemplate
            , @Value("${services.names.user-service}") String userServiceAddress) {
        this.restTemplate = restTemplate;
        this.userServiceAddress = userServiceAddress;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public UserServiceResponse register(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException {
        final String url = userServiceAddress + "/register";

        return getUserServiceResponse(url, usernameAndPasswordDTO);
    }

    @Override
    public UserServiceResponse loginUser(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException {
        final String url = userServiceAddress + "/login";

        return getUserServiceResponse(url, usernameAndPasswordDTO);
    }

    private UserServiceResponse getUserServiceResponse(String url, UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException {
        HttpEntity<String> request = convertToRequest(usernameAndPasswordDTO);
        ResponseEntity<String> userServiceResponseResponseEntity;
        try {
            userServiceResponseResponseEntity = restTemplate.postForEntity(url, request, String.class);
        } catch (HttpClientErrorException exception) {
            logger.warn(exception.getMessage(), exception);
            throw new UserServiceFailedInputException(exception.getResponseBodyAsString(), exception);
        } catch (HttpServerErrorException | UnknownHttpStatusCodeException exception) {
            logger.warn(exception.getMessage(), exception);
            throw new UserServiceConnectionException("Could not connect to user-service", exception);
        }
        HttpStatus statusCode = userServiceResponseResponseEntity.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            try {
                return objectMapper.readValue(userServiceResponseResponseEntity.getBody(), UserServiceResponse.class);
            } catch (JsonProcessingException e) {
                throw new UserServiceException("Could not parse answer from user-service", e);
            }
        } else if (statusCode.is4xxClientError()) {
            try {
                UserServiceErrorDTO errorDTO = objectMapper.readValue(userServiceResponseResponseEntity.getBody(), UserServiceErrorDTO.class);
                throw new UserServiceFailedInputException(errorDTO.getMessage());
            } catch (JsonProcessingException e) {
                throw new UserServiceConnectionException("Could not connect to user-service", e);
            }
        } else {
            throw new UserServiceConnectionException("Could not connect to user-service");
        }
    }

    private HttpEntity<String> convertToRequest(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceFailedInputException {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new HttpEntity<>(objectMapper.writeValueAsString(usernameAndPasswordDTO), headers);
        } catch (JsonProcessingException e) {
            throw new UserServiceFailedInputException("Could not parse User info to json", e);
        }
    }

    @Override
    public UserServiceResponse checkUserByUsernameAndId(long id, String username) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException {
        final String url = userServiceAddress + "/getUserInfo";

        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);

        UserServiceResponse userServiceResponse = getUserServiceResponse(url, usernameAndPasswordDTO);

        if (id != userServiceResponse.getId())
            throw new UserServiceFailedInputException("Id's do not match");

        return userServiceResponse;
    }
}
