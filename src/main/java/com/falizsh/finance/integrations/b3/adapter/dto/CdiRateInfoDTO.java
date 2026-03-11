package com.falizsh.finance.integrations.b3.adapter.dto;

import java.math.BigDecimal;

public record CdiRateInfoDTO(
        String date,
        BigDecimal dailyRate,
        BigDecimal annualRate
) {
}
