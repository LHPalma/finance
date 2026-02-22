package com.falizsh.finance.users.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "{validation.not.blank}")
        String name,

        @NotBlank(message = "{validation.not.blank}")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "{validation.not.blank}")
        @Size(min = 8, message = "{validation.password.min.length}")
        String password,

        String salt
) {
}
