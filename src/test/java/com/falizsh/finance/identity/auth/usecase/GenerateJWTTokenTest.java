package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.infrastructure.security.jwt.JwtTokenGeneratorAdapter;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenerateJWTTokenTest extends TestSupport {

    private JwtTokenGeneratorAdapter tokenGenerator;
    private static final String TEST_SECRET = "test-secret-key-for-jwt-generation";
    private User testUser;

    @Override
    public void init() {
        tokenGenerator = new JwtTokenGeneratorAdapter();
        ReflectionTestUtils.setField(tokenGenerator, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(tokenGenerator, "expirationHours", 2);
        ReflectionTestUtils.setField(tokenGenerator, "refreshExpirationDays", 14);

        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .build();
    }

    @Test
    void shouldGenerateValidAccessToken() {
        String token = tokenGenerator.generate(testUser);
        DecodedJWT decodedJWT = tokenGenerator.verifyAccessToken(token);

        assertThat(token).isNotBlank();
        assertThat(decodedJWT).isNotNull();
        assertThat(decodedJWT.getSubject()).isEqualTo(testUser.getEmail());
        assertThat(decodedJWT.getClaim("id").asLong()).isEqualTo(testUser.getId());
        assertThat(decodedJWT.getClaim("type").asString()).isEqualTo("access");
        assertThat(decodedJWT.getIssuer()).isEqualTo("finance API");
    }

    @Test
    void shouldGenerateRefreshTokenWithJwtIdAndType() {
        UUID tokenId = UUID.randomUUID();
        String token = tokenGenerator.generateRefreshToken(testUser, tokenId);
        DecodedJWT decodedJWT = tokenGenerator.verifyRefreshToken(token);

        assertThat(decodedJWT).isNotNull();
        assertThat(decodedJWT.getId()).isEqualTo(tokenId.toString());
        assertThat(decodedJWT.getClaim("type").asString()).isEqualTo("refresh");
    }

    @Test
    void shouldRejectAccessTokenWhenSignatureIsInvalid() {
        String token = tokenGenerator.generate(testUser);
        JwtTokenGeneratorAdapter invalidVerifier = new JwtTokenGeneratorAdapter();
        ReflectionTestUtils.setField(invalidVerifier, "secret", "wrong-secret");

        DecodedJWT decoded = invalidVerifier.verifyAccessToken(token);

        assertThat(decoded).isNull();
    }

    @Test
    void shouldRejectExpiredToken() {
        ReflectionTestUtils.setField(tokenGenerator, "expirationHours", -1);
        String expiredToken = tokenGenerator.generate(testUser);

        DecodedJWT decoded = tokenGenerator.verifyAccessToken(expiredToken);

        assertThat(decoded).isNull();
    }

    @Test
    void shouldRejectMalformedToken() {
        assertThat(tokenGenerator.verifyAccessToken("not.a.jwt")).isNull();
    }

    @Test
    void shouldRejectNullToken() {
        assertThat(tokenGenerator.verifyAccessToken(null)).isNull();
    }

    @Test
    void shouldRejectRefreshTokenWhenUsingAccessVerifier() {
        String refreshToken = tokenGenerator.generateRefreshToken(testUser, UUID.randomUUID());

        DecodedJWT decoded = tokenGenerator.verifyAccessToken(refreshToken);

        assertThat(decoded).isNull();
    }

    @Test
    void shouldSetExpirationTimeApproximately2HoursInFuture() {
        Instant beforeGeneration = Instant.now().plusSeconds(7190);

        String token = tokenGenerator.generate(testUser);

        Instant afterGeneration = Instant.now().plusSeconds(7210);
        DecodedJWT decodedJWT = tokenGenerator.verifyAccessToken(token);
        Instant expiresAt = decodedJWT.getExpiresAtAsInstant();

        assertThat(expiresAt)
                .isAfter(beforeGeneration)
                .isBefore(afterGeneration);
    }

    @Test
    void shouldThrowExceptionForNullUserWhenGenerating() {
        assertThatThrownBy(() -> tokenGenerator.generate(null))
                .isInstanceOf(RuntimeException.class);
    }
}
