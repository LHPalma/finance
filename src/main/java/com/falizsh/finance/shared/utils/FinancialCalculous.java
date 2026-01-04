package com.falizsh.finance.shared.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class FinancialCalculous {

    public static BigDecimal calculateAnnualRate(BigDecimal dailyRate) {
        if (dailyRate == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal ONE = BigDecimal.ONE;
        BigDecimal HUNDRED = new BigDecimal("100");
        int BUSINESS_DAYS = 252;

        BigDecimal rateDecimal = dailyRate.divide(HUNDRED, 16, RoundingMode.HALF_UP);

        BigDecimal base = ONE.add(rateDecimal);

        BigDecimal annualizedBase = base.pow(BUSINESS_DAYS, MathContext.DECIMAL128);

        BigDecimal resultDecimal = annualizedBase.subtract(ONE);

        BigDecimal annualRate = resultDecimal.multiply(HUNDRED);

        return annualRate.setScale(2, RoundingMode.HALF_UP);
    }

}
