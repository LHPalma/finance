package com.falizsh.finance.auth.usecase;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.falizsh.finance.users.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class GenerateJWTToken implements GenerateJWTTokenUseCase {

    @Value("${spring.security.token.secret}")
    String secret;

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
        return LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant();
    }

}
