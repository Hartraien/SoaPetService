package ru.hartraien.authservice.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.hartraien.authservice.Utility.TokenTypes;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtilImpl implements JwtUtil {

    private final Logger logger;
    private final int jwtExpirationMS;

    private final Key secretKey;
    private final JwtParser parser;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final String id_claim_name;

    public JwtUtilImpl(@Value("${jwt.expiration}") int jwtExpirationMS, @Value("${id-claim-name}") String id_claim_name) {
        this.jwtExpirationMS = jwtExpirationMS;
        this.id_claim_name = id_claim_name;
        secretKey = Keys.secretKeyFor(signatureAlgorithm);
        parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        logger = LoggerFactory.getLogger(JwtUtilImpl.class);
    }

    @Override
    public String generateToken(long id, String username, TokenTypes type) {
        Map<String, Object> claims = Map.of(id_claim_name, String.valueOf(id));
        Date now = new Date();
        Date expiration;
        if (type == TokenTypes.ACCESS)
            expiration = new Date(now.getTime() + jwtExpirationMS);
        else
            expiration = new Date(now.getTime() + jwtExpirationMS * 2L);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public long getIdFromToken(String token) {
        return Long.parseLong((String) parser.parseClaimsJws(token).getBody().get(id_claim_name));
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            return !isTokenExpired(claimsJws);
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

    private boolean isTokenExpired(Jws<Claims> claimsJws) {
        return claimsJws.getBody().getExpiration().before(new Date());
    }
}
