package com.falizsh.finance.identity.users.userAddress.adapter.dto;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {
}
