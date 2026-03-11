package com.falizsh.finance.integrations.stock.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrapiQuoteResponse(
        List<BrapiStockData> results
) {
}
