package com.falizsh.finance.shared.stock.adapter.dto;

import java.math.BigDecimal;

public record StockInfoDTO(
        String symbol,
        String name,
        BigDecimal price,
        BigDecimal change,
        BigDecimal changePercent,
        String logoUrl
) {
}