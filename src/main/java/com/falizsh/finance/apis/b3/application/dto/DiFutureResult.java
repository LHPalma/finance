package com.falizsh.finance.apis.b3.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DiFutureResult(
        String contractCode,
        LocalDate maturityDate,
        BigDecimal rate,
        BigDecimal unitPrice
) {
}