package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.dto.response.AuthResponse;
import com.falizsh.finance.identity.auth.model.RefreshToken;
import com.falizsh.finance.identity.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthSession implements AuthSessionUseCase {

    private final GenerateJWTTokenUseCase tokenUseCase;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Clock clock;

    @Value("${security.token.refresh-expiration-days:14}")
    private long refreshExpirationDays;

    @Override
    @Transactional
    public AuthResponse createSession(com.falizsh.finance.identity.users.user.model.User user) {
        RefreshToken refreshToken = RefreshToken.create(user, refreshTokenTtl(), clock);
        return buildAuthResponse(refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse refreshSession(String refreshTokenValue) {
        DecodedJWT decodedJWT = requireValidRefreshToken(tokenUseCase.verifyRefreshToken(refreshTokenValue));
        UUID currentTokenId = extractTokenId(decodedJWT);
        RefreshToken currentRefreshToken = refreshTokenRepository.findByTokenId(currentTokenId)
                .orElseThrow(this::invalidRefreshToken);

        Instant now = clock.instant();
        if (currentRefreshToken.isRevoked()) {
            revokeAllActiveTokens(currentRefreshToken.getUser().getId(), now);
            throw invalidRefreshToken();
        }

        if (!currentRefreshToken.isActive(now)) {
            throw invalidRefreshToken();
        }

        RefreshToken replacementRefreshToken = RefreshToken.create(
                currentRefreshToken.getUser(),
                refreshTokenTtl(),
                clock
        );
        currentRefreshToken.revoke(now, replacementRefreshToken.getTokenId());

        return buildAuthResponse(replacementRefreshToken);
    }

    private AuthResponse buildAuthResponse(RefreshToken refreshToken) {
        var user = refreshToken.getUser();
        String accessToken = tokenUseCase.generate(user);
        String refreshTokenValue = tokenUseCase.generateRefreshToken(user, refreshToken.getTokenId());
        requireValidRefreshToken(tokenUseCase.verifyRefreshToken(refreshTokenValue));

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .build();
    }

    private void revokeAllActiveTokens(Long userId, Instant now) {
        List<RefreshToken> activeTokens = refreshTokenRepository.findAllByUserIdAndRevokedAtIsNullAndExpiresAtAfter(userId, now);
        for (RefreshToken activeToken : activeTokens) {
            activeToken.revoke(now, null);
        }
    }

    private DecodedJWT requireValidRefreshToken(DecodedJWT decodedJWT) {
        if (decodedJWT == null) {
            throw invalidRefreshToken();
        }
        return decodedJWT;
    }

    private UUID extractTokenId(DecodedJWT decodedJWT) {
        try {
            return UUID.fromString(decodedJWT.getId());
        } catch (Exception exception) {
            throw invalidRefreshToken();
        }
    }

    private ResponseStatusException invalidRefreshToken() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid.refresh.token");
    }

    private Duration refreshTokenTtl() {
        return Duration.ofDays(refreshExpirationDays);
    }
}
