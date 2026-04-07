package com.falizsh.finance.identity.auth.action;

import com.falizsh.finance.identity.auth.usecase.GenerateJWTToken;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class GetSubjectTest extends TestSupport {

    private GetSubject getSubject;
    private GenerateJWTToken generateJWTToken;

    private static final String TEST_SECRET = "test-secret-key-for-subject-extraction";
    private static final String TEST_EMAIL = "test@email.com";

    @Override
    public void init() {
        this.generateJWTToken = new GenerateJWTToken();
        ReflectionTestUtils.setField(generateJWTToken, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(generateJWTToken, "expirationHours", 2);
        ReflectionTestUtils.setField(generateJWTToken, "refreshExpirationDays", 14);
        this.getSubject = new GetSubject(generateJWTToken);
    }

    @Test
    void shouldExtractSubjectFromValidToken() {
        String validToken = generateAccessToken(TEST_EMAIL);
        String subject = getSubject.from(validToken);

        assertThat(subject).isEqualTo(TEST_EMAIL);
    }

    @Test
    void shouldReturnNullForExpiredToken() {
        ReflectionTestUtils.setField(generateJWTToken, "expirationHours", -1);
        String expiredToken = generateAccessToken(TEST_EMAIL);
        String subject = getSubject.from(expiredToken);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForRefreshToken() {
        String refreshToken = generateJWTToken.generateRefreshToken(
                valid(User.class).toBuilder().email(TEST_EMAIL).build(),
                UUID.randomUUID()
        );

        String subject = getSubject.from(refreshToken);

        assertThat(subject).isNull();
    }

    @Test
    void shouldReturnNullForTokenWithWrongSignature() {
        GenerateJWTToken wrongSigner = new GenerateJWTToken();
        ReflectionTestUtils.setField(wrongSigner, "secret", "wrong-secret");
        ReflectionTestUtils.setField(wrongSigner, "expirationHours", 2);
        ReflectionTestUtils.setField(wrongSigner, "refreshExpirationDays", 14);
        String tokenWithWrongSignature = wrongSigner.generate(
                valid(User.class).toBuilder().email(TEST_EMAIL).build()
        );

        String subject = getSubject.from(tokenWithWrongSignature);

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
        String tokenWithoutExpiration = "not.supported.anymore";
        String subject = getSubject.from(tokenWithoutExpiration);

        assertThat(subject).isNull();
    }

    private String generateAccessToken(String email) {
        return generateJWTToken.generate(
                valid(User.class).toBuilder().email(email).build()
        );
    }
}
