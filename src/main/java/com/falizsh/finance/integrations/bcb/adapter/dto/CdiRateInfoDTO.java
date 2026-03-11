package com.falizsh.finance.integrations.bcb.adapter.dto;

import java.math.BigDecimal;

public record CdiRateInfoDTO(
        String date,
        BigDecimal dailyRate,
        BigDecimal annualRate
) {
}
