package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.users.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class GenerateJWTToken implements GenerateJWTTokenUseCase {

    private static final String ISSUER = "finance API";
    private static final String CLAIM_TOKEN_TYPE = "type";
    private static final String CLAIM_USER_ID = "id";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    @Value("${security.token.secret}")
    String secret;

    @Value("${security.token.expiration-hours:2}")
    int expirationHours;

    @Value("${security.token.refresh-expiration-days:14}")
    int refreshExpirationDays;

    @Override
    public String generate(User user) {
        return createToken(user, getAccessExpirationDate(), ACCESS_TOKEN, null);
    }

    @Override
    public String generateRefreshToken(User user, UUID tokenId) {
        return createToken(user, getRefreshExpirationDate(), REFRESH_TOKEN, tokenId);
    }

    @Override
    public DecodedJWT verifyAccessToken(String token) {
        return verify(token, ACCESS_TOKEN);
    }

    @Override
    public DecodedJWT verifyRefreshToken(String token) {
        return verify(token, REFRESH_TOKEN);
    }

    private String createToken(User user, Instant expiresAt, String tokenType, UUID tokenId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var builder = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withClaim(CLAIM_USER_ID, user.getId())
                    .withClaim(CLAIM_TOKEN_TYPE, tokenType)
                    .withExpiresAt(expiresAt);

            if (tokenId != null) {
                builder.withJWTId(tokenId.toString());
            }

            return builder.sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("failed.to.generate.jwt.token:", exception);
        }
    }

    private DecodedJWT verify(String token, String expectedType) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(CLAIM_TOKEN_TYPE, expectedType)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant getAccessExpirationDate() {
        return LocalDateTime.now()
                .plusHours(expirationHours)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    private Instant getRefreshExpirationDate() {
        return LocalDateTime.now()
                .plusDays(refreshExpirationDays)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

}
