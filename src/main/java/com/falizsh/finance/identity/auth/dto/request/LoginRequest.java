package com.falizsh.finance.identity.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotNull
        @Email
        String email,

        @NotNull
        // @Size(min = 8, max = 24)
        String password

) {
}
