package com.falizsh.finance.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token
) {
}
