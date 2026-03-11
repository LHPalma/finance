package com.falizsh.finance.analytics;

import java.math.BigDecimal;

public record SimulationResult(
        BigDecimal buyPrice,
        BigDecimal sellPrice,
        BigDecimal grossProfit,
        BigDecimal profitability
) {
}
