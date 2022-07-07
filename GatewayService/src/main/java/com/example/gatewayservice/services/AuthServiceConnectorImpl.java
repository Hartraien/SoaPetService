package com.example.gatewayservice.services;

import com.example.gatewayservice.DTOs.AuthServiceResponse;
import com.example.gatewayservice.DTOs.UserServiceResponse;
import com.example.gatewayservice.exceptions.AuthServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceConnectorImpl implements AuthServiceConnector {
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    private final ObjectMapper objectMapper;

    @Autowired
    public AuthServiceConnectorImpl(RestTemplate restTemplate
            , @Value("services.auth-service-url") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AuthServiceResponse getInfoFromToken(String token) throws AuthServiceException {
        String url = authServiceUrl + "/verifyToken";
        TokenRequest tokenRequest = new TokenRequest(token);

        ResponseEntity<String> userServiceResponseResponseEntity = restTemplate.postForEntity(url, tokenRequest, String.class);
        HttpStatus statusCode = userServiceResponseResponseEntity.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            try {
                return objectMapper.readValue(userServiceResponseResponseEntity.getBody(), AuthServiceResponse.class);
            } catch (JsonProcessingException e) {
                throw new AuthServiceException("Could not parse answer from auth-service", e);
            }
        } else if (statusCode.is4xxClientError()) {
            try {
                AuthServiceErrorDTO errorDTO = objectMapper.readValue(userServiceResponseResponseEntity.getBody(), AuthServiceErrorDTO.class);
                throw new AuthServiceException(errorDTO.getMessage());
            } catch (JsonProcessingException e) {
                throw new AuthServiceException("Could not connect to auth-service", e);
            }
        } else {
            throw new AuthServiceException("Could not connect to auth-service");
        }
    }
}
