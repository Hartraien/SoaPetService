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
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtilImpl implements JwtUtil {

    private final Logger logger;
    private final long jwtExpirationMilliSeconds;

    private final Key secretKey;
    private final JwtParser parser;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final String id_claim_name;

    public JwtUtilImpl(@Value("${jwt.expiration-seconds}") int jwtExpirationSeconds, @Value("${id-claim-name}") String id_claim_name) {
        this.jwtExpirationMilliSeconds = TimeUnit.MILLISECONDS.convert(jwtExpirationSeconds, TimeUnit.SECONDS);
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
            expiration = new Date(now.getTime() + jwtExpirationMilliSeconds);
        else
            expiration = new Date(now.getTime() + jwtExpirationMilliSeconds * 2L);
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
            logger.debug("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            logger.debug("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.debug("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
    }

    private boolean isTokenExpired(Jws<Claims> claimsJws) {
        return claimsJws.getBody().getExpiration().before(new Date());
    }
}
