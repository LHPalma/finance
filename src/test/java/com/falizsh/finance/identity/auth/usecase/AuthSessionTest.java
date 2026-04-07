package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.dto.response.AuthResponse;
import com.falizsh.finance.identity.auth.model.RefreshToken;
import com.falizsh.finance.identity.auth.repository.RefreshTokenRepository;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

class AuthSessionTest extends TestSupport {

    @Mock
    private GenerateJWTTokenUseCase tokenUseCase;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private AuthSession authSession;
    private User user;
    private Clock clock;

    @Override
    public void init() {
        clock = Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), ZoneOffset.UTC);
        authSession = new AuthSession(tokenUseCase, refreshTokenRepository, clock);
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

        when(tokenUseCase.generate(user)).thenReturn(accessTokenValue);
        when(tokenUseCase.generateRefreshToken(eq(user), any(UUID.class))).thenReturn(refreshTokenValue);
        when(tokenUseCase.verifyRefreshToken(refreshTokenValue)).thenReturn(decodedRefreshToken);

        AuthResponse response = authSession.createSession(user);

        assertThat(response.accessToken()).isEqualTo(accessTokenValue);
        assertThat(response.refreshToken()).isEqualTo(refreshTokenValue);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getExpiresAt()).isEqualTo(clock.instant().plus(Duration.ofDays(14)));
    }

    @Test
    void shouldRotateRefreshToken() {
        String currentRefreshTokenValue = "refresh.token";
        String newRefreshTokenValue = "refresh.token.new";
        String accessTokenValue = "access.token.new";
        RefreshToken currentRefreshToken = RefreshToken.create(user, Duration.ofDays(14), clock);
        DecodedJWT currentDecodedJwt = mock(DecodedJWT.class);

        when(tokenUseCase.verifyRefreshToken(currentRefreshTokenValue)).thenReturn(currentDecodedJwt);
        when(currentDecodedJwt.getId()).thenReturn(currentRefreshToken.getTokenId().toString());
        when(refreshTokenRepository.findByTokenId(currentRefreshToken.getTokenId())).thenReturn(Optional.of(currentRefreshToken));
        when(tokenUseCase.generate(user)).thenReturn(accessTokenValue);
        when(tokenUseCase.generateRefreshToken(eq(user), any(UUID.class))).thenReturn(newRefreshTokenValue);
        when(tokenUseCase.verifyRefreshToken(newRefreshTokenValue)).thenReturn(mock(DecodedJWT.class));

        AuthResponse response = authSession.refreshSession(currentRefreshTokenValue);

        assertThat(response.accessToken()).isEqualTo(accessTokenValue);
        assertThat(response.refreshToken()).isEqualTo(newRefreshTokenValue);
        assertThat(currentRefreshToken.isRevoked()).isTrue();
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldRejectReusedRefreshToken() {
        String refreshTokenValue = "refresh.token";
        RefreshToken currentRefreshToken = RefreshToken.create(user, Duration.ofDays(14), clock);
        currentRefreshToken.revoke(clock.instant(), UUID.randomUUID());
        DecodedJWT decodedJwt = mock(DecodedJWT.class);
        RefreshToken activeToken = RefreshToken.create(user, Duration.ofDays(14), clock);

        when(tokenUseCase.verifyRefreshToken(refreshTokenValue)).thenReturn(decodedJwt);
        when(decodedJwt.getId()).thenReturn(currentRefreshToken.getTokenId().toString());
        when(refreshTokenRepository.findByTokenId(currentRefreshToken.getTokenId())).thenReturn(Optional.of(currentRefreshToken));
        when(refreshTokenRepository.findAllByUserIdAndRevokedAtIsNullAndExpiresAtAfter(eq(user.getId()), eq(clock.instant())))
                .thenReturn(List.of(activeToken));

        assertThatThrownBy(() -> authSession.refreshSession(refreshTokenValue))
                .isInstanceOf(ResponseStatusException.class);

        assertThat(activeToken.isRevoked()).isTrue();
    }
}
