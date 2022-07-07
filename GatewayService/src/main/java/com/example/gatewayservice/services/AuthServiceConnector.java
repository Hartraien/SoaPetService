package com.example.gatewayservice.services;

import com.example.gatewayservice.exceptions.AuthServiceException;
import com.example.gatewayservice.DTOs.AuthServiceResponse;

public interface AuthServiceConnector {
    AuthServiceResponse getInfoFromToken(String token) throws AuthServiceException;
}
