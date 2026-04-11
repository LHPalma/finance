package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.application.dto.response.AuthResponse;
import com.falizsh.finance.identity.auth.application.port.TokenGenerator;
import com.falizsh.finance.identity.auth.application.query.handler.FindActiveRefreshTokensByUserIdQueryHandler;
import com.falizsh.finance.identity.auth.application.query.handler.FindRefreshTokenByTokenIdQueryHandler;
import com.falizsh.finance.identity.auth.application.usecase.AuthSession;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenRepository;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthSessionTest extends TestSupport {

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private FindRefreshTokenByTokenIdQueryHandler findRefreshTokenByTokenIdQueryHandler;

    @Mock
    private FindActiveRefreshTokensByUserIdQueryHandler findActiveRefreshTokensByUserIdQueryHandler;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private AuthSession authSession;
    private User user;
    private Clock clock;

    @Override
    public void init() {
        clock = Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), ZoneOffset.UTC);
        authSession = new AuthSession(
                tokenGenerator,
                findRefreshTokenByTokenIdQueryHandler,
                findActiveRefreshTokensByUserIdQueryHandler,
                refreshTokenRepository,
                clock
        );
        ReflectionTestUtils.setField(authSession, "refreshExpirationDays", 14L);
        user = User.builder()
                .id(10L)
                .email("user@email.com")
                .name("User")
                .build();
    }

    @Test
    void shouldCreateSessionAndPersistRefreshToken() {
        String refreshTokenValue = "refresh.token";
        String accessTokenValue = "access.token";
        DecodedJWT decodedRefreshToken = mock(DecodedJWT.class);

        when(tokenGenerator.generate(user)).thenReturn(accessTokenValue);
        when(tokenGenerator.generateRefreshToken(eq(user), any(UUID.class))).thenReturn(refreshTokenValue);
        when(tokenGenerator.verifyRefreshToken(refreshTokenValue)).thenReturn(decodedRefreshToken);

        AuthResponse response = authSession.createSession(user);

        assertThat(response.accessToken()).isEqualTo(accessTokenValue);
        assertThat(response.refreshToken()).isEqualTo(refreshTokenValue);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        InOrder inOrder = inOrder(tokenGenerator, refreshTokenRepository);
        inOrder.verify(tokenGenerator).generate(user);
        inOrder.verify(tokenGenerator).generateRefreshToken(eq(user), any(UUID.class));
        inOrder.verify(tokenGenerator).verifyRefreshToken(refreshTokenValue);
        inOrder.verify(refreshTokenRepository).save(captor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getExpiresAt()).isEqualTo(clock.instant().plus(Duration.ofDays(14)));
    }

    @Test
    void shouldRotateRefreshTokenWhenTokenIsActive() {
        String currentRefreshTokenValue = "refresh.token";
        String newRefreshTokenValue = "refresh.token.new";
        String accessTokenValue = "access.token.new";
        RefreshToken currentRefreshToken = RefreshToken.create(user, Duration.ofDays(14), clock);
        DecodedJWT currentDecodedJwt = mock(DecodedJWT.class);

        when(tokenGenerator.verifyRefreshToken(currentRefreshTokenValue)).thenReturn(currentDecodedJwt);
        when(currentDecodedJwt.getId()).thenReturn(currentRefreshToken.getTokenId().toString());
        when(findRefreshTokenByTokenIdQueryHandler.handle(any()))
                .thenReturn(Optional.of(currentRefreshToken));
        when(tokenGenerator.generate(user)).thenReturn(accessTokenValue);
        when(tokenGenerator.generateRefreshToken(eq(user), any(UUID.class))).thenReturn(newRefreshTokenValue);
        when(tokenGenerator.verifyRefreshToken(newRefreshTokenValue)).thenReturn(mock(DecodedJWT.class));

        AuthResponse response = authSession.refreshSession(currentRefreshTokenValue);

        assertThat(response.accessToken()).isEqualTo(accessTokenValue);
        assertThat(response.refreshToken()).isEqualTo(newRefreshTokenValue);
        assertThat(currentRefreshToken.isRevoked()).isTrue();
        assertThat(currentRefreshToken.getReplacedByTokenId()).isNotNull();
    }

    @Test
    void shouldRejectRefreshWhenDecoderReturnsNull() {
        when(tokenGenerator.verifyRefreshToken("invalid.token")).thenReturn(null);

        assertThatThrownBy(() -> authSession.refreshSession("invalid.token"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectRefreshWhenTokenIdIsMalformed() {
        DecodedJWT decodedJwt = mock(DecodedJWT.class);
        when(tokenGenerator.verifyRefreshToken("refresh.token")).thenReturn(decodedJwt);
        when(decodedJwt.getId()).thenReturn("not-a-uuid");

        assertThatThrownBy(() -> authSession.refreshSession("refresh.token"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectRefreshWhenTokenWasNotFound() {
        DecodedJWT decodedJwt = mock(DecodedJWT.class);
        UUID tokenId = UUID.randomUUID();

        when(tokenGenerator.verifyRefreshToken("refresh.token")).thenReturn(decodedJwt);
        when(decodedJwt.getId()).thenReturn(tokenId.toString());
        when(findRefreshTokenByTokenIdQueryHandler.handle(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authSession.refreshSession("refresh.token"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectRefreshWhenTokenIsExpired() {
        DecodedJWT decodedJwt = mock(DecodedJWT.class);
        RefreshToken expiredToken = RefreshToken.create(user, Duration.ofHours(1), clock);
        expiredToken.revoke(null, null);
        ReflectionTestUtils.setField(expiredToken, "revokedAt", null);
        ReflectionTestUtils.setField(expiredToken, "expiresAt", clock.instant().minusSeconds(1));

        when(tokenGenerator.verifyRefreshToken("refresh.token")).thenReturn(decodedJwt);
        when(decodedJwt.getId()).thenReturn(expiredToken.getTokenId().toString());
        when(findRefreshTokenByTokenIdQueryHandler.handle(any())).thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> authSession.refreshSession("refresh.token"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectReusedRefreshTokenAndRevokeAllActives() {
        String refreshTokenValue = "refresh.token";
        RefreshToken currentRefreshToken = RefreshToken.create(user, Duration.ofDays(14), clock);
        currentRefreshToken.revoke(clock.instant(), UUID.randomUUID());
        DecodedJWT decodedJwt = mock(DecodedJWT.class);
        RefreshToken activeToken = RefreshToken.create(user, Duration.ofDays(14), clock);

        when(tokenGenerator.verifyRefreshToken(refreshTokenValue)).thenReturn(decodedJwt);
        when(decodedJwt.getId()).thenReturn(currentRefreshToken.getTokenId().toString());
        when(findRefreshTokenByTokenIdQueryHandler.handle(any())).thenReturn(Optional.of(currentRefreshToken));
        when(findActiveRefreshTokensByUserIdQueryHandler.handle(any())).thenReturn(List.of(activeToken));

        assertThatThrownBy(() -> authSession.refreshSession(refreshTokenValue))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(activeToken.isRevoked()).isTrue();
    }
}
