package com.falizsh.finance.simulation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record SimulationRequest(
        BigDecimal vna,
        BigDecimal annualRate,
        BigDecimal projectedInflation,
        LocalDate calculationDate,
        LocalDate maturityDate
) {

    @Override
    public BigDecimal projectedInflation() {
        return Objects.requireNonNullElse(projectedInflation, BigDecimal.ZERO);
    }
}