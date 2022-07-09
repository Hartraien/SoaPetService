package ru.hartraien.authservice.Service;

import ru.hartraien.authservice.Utility.TokenTypes;

public interface JwtUtil {
    String generateToken(long id, String username, TokenTypes type);

    String getUsernameFromToken(String token);

    long getIdFromToken(String token);

    boolean validateToken(String token);
}
