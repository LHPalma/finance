package com.falizsh.finance.apis.b3.adapter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DiFutureResponse(
        String contractCode,
        LocalDate maturityDate,
        BigDecimal rate,
        BigDecimal unitPrice
) {}