package com.falizsh.finance.shared.bcb.adapter.dto;

import java.math.BigDecimal;

public record SelicRateInfoDTO(
        String date,
        BigDecimal dailyRate,
        BigDecimal annualRate
) {
}
