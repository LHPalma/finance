package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.falizsh.finance.identity.users.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class GenerateJWTToken implements GenerateJWTTokenUseCase {

    @Value("${security.token.secret}")
    String secret;

    @Value("${security.token.expiration-hours:2}")
    int expirationHours;

    @Override
    public String generate(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("finance API")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("failed.to.generate.jwt.token:", exception);
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now()
                .plusHours(expirationHours)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

}
