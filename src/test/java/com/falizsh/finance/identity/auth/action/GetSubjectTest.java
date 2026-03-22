package com.falizsh.finance.identity.auth.action;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

class GetSubjectTest extends TestSupport {

    private GetSubject getSubject;

    private static final String TEST_SECRET = "test-secret-key-for-subject-extraction";
    private static final String TEST_EMAIL = "test@email.com";

    @Override
    public void init() {
        this.getSubject = new GetSubject();
        ReflectionTestUtils.setField(getSubject, "secret", TEST_SECRET);
    }

    @Test
    void shouldExtractSubjectFromValidToken() {
        String validToken = generateValidToken(TEST_EMAIL);
        String subject = getSubject.from(validToken);

        assertThat(subject).isEqualTo(TEST_EMAIL);
    }

    @Test
    void shouldReturnNullForExpiredToken() {
        String expiredToken = generateExpiredToken(TEST_EMAIL);
        String subject = getSubject.from(expiredToken);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForTokenWithWrongSignature() {
        Algorithm wrongAlgorithm = Algorithm.HMAC256("wrong-secret");

        String tokenWithWrongSignature = JWT.create()
                .withIssuer("finance API")
                .withSubject(TEST_EMAIL)
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(wrongAlgorithm);

        String subject = getSubject.from(tokenWithWrongSignature);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForTokenWithWrongIssuer() {
        Algorithm algorithm = Algorithm.HMAC256(TEST_SECRET);

        String tokenWithWrongIssuer = JWT.create()
                .withIssuer("wrong-issuer")
                .withSubject(TEST_EMAIL)
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);

        String subject = getSubject.from(tokenWithWrongIssuer);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForMalformedToken() {
        String malformedToken = "not.a.valid.jwt.token";
        String subject = getSubject.from(malformedToken);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForNullToken() {
        String subject = getSubject.from(null);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForEmptyToken() {
        String subject = getSubject.from("");

        assertThat(subject).isNull();
    }

    @Test
    void shouldHandleTokenWithoutExpirationGracefully() {
        Algorithm algorithm = Algorithm.HMAC256(TEST_SECRET);

        String tokenWithoutExpiration = JWT.create()
                .withIssuer("finance API")
                .withSubject(TEST_EMAIL)
                .sign(algorithm);

        String subject = getSubject.from(tokenWithoutExpiration);

        assertThat(subject).isEqualTo(TEST_EMAIL);
    }

    private String generateValidToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(TEST_SECRET);

        return JWT.create()
                .withIssuer("finance API")
                .withSubject(email)
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    private String generateExpiredToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(TEST_SECRET);

        return JWT.create()
                .withIssuer("finance API")
                .withSubject(email)
                .withExpiresAt(Instant.now().minus(1, ChronoUnit.HOURS))
                .sign(algorithm);
    }
}