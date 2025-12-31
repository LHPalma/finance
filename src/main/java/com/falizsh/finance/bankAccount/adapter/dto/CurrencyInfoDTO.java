package com.falizsh.finance.bankAccount.adapter.dto;

/**
 * DTO que representa as informações de cotação de moedas retornadas pela API externa.
 *
 * @param code        Código da moeda de origem (ex: USD).
 * @param codein      Código da moeda de destino (ex: BRL).
 * @param name        Nome formatado da paridade (ex: Dólar Americano/Real Brasileiro).
 * @param high        Valor máximo registrado no dia.
 * @param low         Valor mínimo registrado no dia.
 * @param bid         Valor de compra (oferta de compra).
 * @param ask         Valor de venda (oferta de venda).
 * @param timestamp   Data/hora da cotação em formato Unix Timestamp.
 * @param create_date Data de criação da cotação em formato legível (String).
 */
public record CurrencyInfoDTO(
        String code,
        String codein,
        String name,
        String high,
        String low,
        String bid,
        String ask,
        String timestamp,
        String create_date
) {
}
