package com.falizsh.finance.users.userEmail.dto;

import com.falizsh.finance.users.userEmail.model.UserEmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailCreateDTO {

    @NotNull(message = "Tipo do email é obrigatório")
    private UserEmailType type;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email é obrigatório")
    private String email;

    private Boolean isPrimary = false;

}
