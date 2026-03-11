package com.falizsh.finance.identity.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token
) {
}
