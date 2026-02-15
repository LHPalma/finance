package com.falizsh.finance.shared.bcbifdata.dto;

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
