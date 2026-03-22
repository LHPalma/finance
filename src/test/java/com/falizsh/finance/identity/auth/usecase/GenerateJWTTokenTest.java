package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class GenerateJWTTokenTest extends TestSupport {

    private GenerateJWTToken generateJWTToken;

    private static final String TEST_SECRET = "test-secret-key-for-jwt-generation";
    private User testUser;

    @Override
    public void init() {
        this.generateJWTToken = new GenerateJWTToken();
        ReflectionTestUtils.setField(generateJWTToken, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(generateJWTToken, "expirationHours", 2);
        
        this.testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .build();
    }

    @Test
    void shouldGenerateValidJwtToken() {
        String token = generateJWTToken.generate(testUser);

        assertThat(token)
                .isNotNull()
                .isNotEmpty();

        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void shouldIncludeUserEmailAsSubject() {
        String token = generateJWTToken.generate(testUser);
        DecodedJWT decodedJWT = JWT.decode(token);

        assertThat(decodedJWT.getSubject()).isEqualTo(testUser.getEmail());
    }

    @Test
    void shouldIncludeUserIdInClaims() {
        String token = generateJWTToken.generate(testUser);
        DecodedJWT decodedJWT = JWT.decode(token);

        assertThat(decodedJWT.getClaim("id").asLong()).isEqualTo(testUser.getId());
    }

    @Test
    void shouldIncludeCorrectIssuer() {
        String token = generateJWTToken.generate(testUser);
        DecodedJWT decodedJWT = JWT.decode(token);

        assertThat(decodedJWT.getIssuer()).isEqualTo("finance API");
    }

    @Test
    void shouldSetExpirationTimeApproximately2HoursInFuture() {
        Instant beforeGeneration = Instant.now().plusSeconds(7195);

        String token = generateJWTToken.generate(testUser);

        Instant afterGeneration = Instant.now().plusSeconds(7205);

        DecodedJWT decodedJWT = JWT.decode(token);
        Instant expiresAt = decodedJWT.getExpiresAtAsInstant();

        assertThat(expiresAt)
                .isAfter(beforeGeneration)
                .isBefore(afterGeneration);
    }

    @Test
    void shouldThrowExceptionForNullUser() {
        assertThatThrownBy(() -> generateJWTToken.generate(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldGenerateDifferentTokensForSameUserAtDifferentTimes() throws InterruptedException {
        String token1 = generateJWTToken.generate(testUser);

        Thread.sleep(1000);

        String token2 = generateJWTToken.generate(testUser);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {
        User user1 = User.builder().id(1L).email("user1@email.com").build();
        User user2 = User.builder().id(2L).email("user2@email.com").build();

        String token1 = generateJWTToken.generate(user1);
        String token2 = generateJWTToken.generate(user2);

        assertThat(token1).isNotEqualTo(token2);

        DecodedJWT decoded1 = JWT.decode(token1);
        DecodedJWT decoded2 = JWT.decode(token2);

        assertThat(decoded1.getSubject()).isEqualTo(user1.getEmail());
        assertThat(decoded2.getSubject()).isEqualTo(user2.getEmail());
    }
}