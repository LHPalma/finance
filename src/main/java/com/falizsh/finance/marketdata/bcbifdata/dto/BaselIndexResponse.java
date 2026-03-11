package com.falizsh.finance.marketdata.bcbifdata.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BaselIndexResponse(
        String institution,
        String referenceDate,
        BigDecimal baselIndex,
        BigDecimal immobilizationIndex
) {
}
