package ru.hartraien.authservice.Service;

public interface JwtUtil {
    String generateToken(long id, String username, String type);

    String getUsernameFromToken(String token);

    long getIdFromToken(String token);

    boolean validateToken(String token);
}
