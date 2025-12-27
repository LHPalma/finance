package com.falizsh.finance.userTelephone.dto;

import com.falizsh.finance.userTelephone.model.TelephoneType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.falizsh.finance.userTelephone.model.UserTelephone.DEFAULT_IS_PRIMARY;
import static com.falizsh.finance.userTelephone.model.UserTelephone.DEFAULT_TYPE;

public record UserTelephoneCreateRequest(

        TelephoneType type,

        @Size(max = 5)
        @NotBlank(message = "O código de área é obrigatório")
        String areaCode,

        @Size(max = 15)
        @NotBlank(message = "O número de telefone é obrigatório")
        String telephone,

        Boolean isPrimary

) {
    public UserTelephoneCreateRequest {
        type = type != null ? type : DEFAULT_TYPE;
        isPrimary = isPrimary != null ? isPrimary : DEFAULT_IS_PRIMARY;
    }
}