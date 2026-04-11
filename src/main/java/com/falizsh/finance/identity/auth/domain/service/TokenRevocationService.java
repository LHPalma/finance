package com.falizsh.finance.identity.auth.domain.service;

import com.falizsh.finance.identity.auth.application.query.FindActiveRefreshTokensByUserIdQuery;
import com.falizsh.finance.identity.auth.application.query.handler.FindActiveRefreshTokensByUserIdQueryHandler;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenRevocationService {

    private final FindActiveRefreshTokensByUserIdQueryHandler findActiveRefreshTokensByUserIdQueryHandler;
    private final Clock clock;

    public void revokeAllActiveTokens(Long userId) {
        Instant now = clock.instant();
        List<RefreshToken> activeTokens = findActiveRefreshTokensByUserIdQueryHandler.handle(
                new FindActiveRefreshTokensByUserIdQuery(userId, now)
        );

        for (RefreshToken activeToken : activeTokens) {
            activeToken.revoke(now, null);
        }
    }
}
