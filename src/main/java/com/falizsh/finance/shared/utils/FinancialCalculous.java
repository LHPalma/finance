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

    /**
     * Calcula o Preço Unitário (PU) de um título IPCA+ (NTN-B Principal).
     * @param vna Valor Nominal Atualizado (ex: R$ 4.300,00)
     * @param annualYield A taxa que o usuário informou (ex: 0.07 para 7% ou 0.06 para 6%)
     * @param businessDays Dias úteis entre a data de cálculo e o vencimento (du)
     * <br />Fórmula: PU = VNA / (1 + taxa)^(du/252)
     * @return O preço do título
     */
    public static BigDecimal calculateNtnbUnitPrice(BigDecimal vna, BigDecimal annualYield, int businessDays) {
        BigDecimal yieldPlusOne = annualYield.add(BigDecimal.ONE);

        double exponent = (double) businessDays / 252.0;

        BigDecimal discountFactor = BigDecimal.valueOf(Math.pow(yieldPlusOne.doubleValue(), exponent))
                .setScale(9, RoundingMode.HALF_UP);

        return vna.divide(discountFactor, 6, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateProjectedVna(BigDecimal vnaBase, BigDecimal ipcaProj, int daysSinceBase, int totalDaysInMonth) {
        double factor = Math.pow(1.0 + ipcaProj.doubleValue(), (double) daysSinceBase / totalDaysInMonth);
        return vnaBase.multiply(BigDecimal.valueOf(factor)).setScale(6, RoundingMode.HALF_UP);
    }

}
