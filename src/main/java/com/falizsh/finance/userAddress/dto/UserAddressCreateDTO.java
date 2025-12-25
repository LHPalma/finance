package com.falizsh.finance.userAddress.dto;

import com.falizsh.finance.userAddress.model.UserAddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserAddressCreateDTO(
        @NotNull
        UserAddressType type,

        @NotBlank(message = "A rua é obrigatória")
        String street,

        @NotBlank(message = "O número é obrigatório")
        @Size(max = 10, message = "Tamanho máximo para o campo número: 10 caractéres")
        String number,

        String complement,

        String neighborhood,

        @NotBlank(message = "A cidade é obrigatória")
        String city,

        @NotBlank(message = "A UF é obrigatória")
        @Size(min = 2, max = 2, message = "Insira a UF com dois caractéres")
        String state,

        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{8}|\\d{5}-\\d{3}", message = "O CEP deve estar no formato 12345678 ou 12345-678")
        String zipCode,

        @Size(min = 3, max = 3, message = "Insira o país com 3 caractéres")
        String country,

        Boolean isPrimary

) {
}
