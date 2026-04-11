package com.falizsh.finance.identity.auth.application.query;

import java.util.UUID;

public record FindRefreshTokenByTokenIdQuery(UUID tokenId) {
}
