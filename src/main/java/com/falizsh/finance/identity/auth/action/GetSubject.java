package com.falizsh.finance.identity.auth.action;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetSubject implements GetSubjectAction {

    @Value("${spring.security.token.secret}")
    String secret;

    @Override
    public String from(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("finance API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return null;
            // throw new RuntimeException("jwt.validation.error", exception);
        }
    }
}
