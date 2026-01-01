package com.falizsh.finance.shared.brapiAPIAdapter.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrapiStockData(
        String symbol,
        String shortName,
        String longName,
        BigDecimal regularMarketPrice,
        BigDecimal regularMarketDayHigh,
        BigDecimal regularMarketDayLow,
        @JsonAlias("regularMarketChange") BigDecimal change,
        @JsonAlias("regularMarketChangePercent") BigDecimal changePercent,
        String logourl
) {}