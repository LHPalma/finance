package com.falizsh.finance.identity.auth.domain.service;

import com.falizsh.finance.identity.auth.application.query.handler.FindActiveRefreshTokensByUserIdQueryHandler;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TokenRevocationServiceTest extends TestSupport {

    @Mock
    private FindActiveRefreshTokensByUserIdQueryHandler findActiveRefreshTokensByUserIdQueryHandler;

    private TokenRevocationService tokenRevocationService;
    private Clock clock;
    private User user;

    @Override
    public void init() {
        clock = Clock.fixed(Instant.parse("2026-04-11T00:00:00Z"), ZoneOffset.UTC);
        tokenRevocationService = new TokenRevocationService(findActiveRefreshTokensByUserIdQueryHandler, clock);
        user = User.builder().id(41L).email("token.revocation@email.com").name("Token User").build();
    }

    @Test
    void shouldRevokeAllActiveTokens() {
        RefreshToken token1 = RefreshToken.create(user, Duration.ofDays(14), clock);
        RefreshToken token2 = RefreshToken.create(user, Duration.ofDays(14), clock);
        when(findActiveRefreshTokensByUserIdQueryHandler.handle(any())).thenReturn(List.of(token1, token2));

        tokenRevocationService.revokeAllActiveTokens(user.getId());

        assertThat(token1.isRevoked()).isTrue();
        assertThat(token2.isRevoked()).isTrue();
    }
}
