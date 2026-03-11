package com.falizsh.finance.marketdata.stock.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrapiQuoteResponse(
        List<BrapiStockData> results
) {
}
