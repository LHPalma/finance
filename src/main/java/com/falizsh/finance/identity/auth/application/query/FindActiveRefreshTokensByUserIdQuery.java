package com.falizsh.finance.identity.auth.application.query;

import java.time.Instant;

public record FindActiveRefreshTokensByUserIdQuery(
        Long userId,
        Instant now
) {
}
