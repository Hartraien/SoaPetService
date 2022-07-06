package ru.hartraien.authservice.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtilImpl implements JwtUtil {

    private final Logger logger;
    private final int jwtExpirationMS;

    private final Key secretKey;
    private final JwtParser parser;

    public JwtUtilImpl(@Value("${jwt.expiration}") int jwtExpirationMS) {
        this.jwtExpirationMS = jwtExpirationMS;
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        logger = LoggerFactory.getLogger(JwtUtilImpl.class);
    }

    @Override
    public String generateToken(long id, String username, String type) {
        Map<String, Object> claims = Map.of("id", id);
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationMS))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return parser.parseClaimsJwt(token).getBody().getSubject();
    }

    @Override
    public long getIdFromToken(String token) {
        return Long.parseLong((String) parser.parseClaimsJwt(token).getBody().get("id"));
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parser.parseClaimsJwt(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
