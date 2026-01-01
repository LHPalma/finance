package com.falizsh.finance.shared.stock.adapter.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YahooChartResponse(
        YahooChartData chart
) {

    public record YahooChartData(
            List<YahooResult> result,
            Object error
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooResult(
            YahooMeta meta
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooMeta(
            String symbol,
            @JsonAlias("longName") String name,
            String currency,
            BigDecimal regularMarketPrice,
            BigDecimal chartPreviousClose
    ) {
    }
}