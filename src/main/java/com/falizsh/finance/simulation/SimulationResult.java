package com.falizsh.finance.simulation;

import java.math.BigDecimal;

public record SimulationResult(
        BigDecimal buyPrice,
        BigDecimal sellPrice,
        BigDecimal grossProfit,
        BigDecimal profitability
) {
}
