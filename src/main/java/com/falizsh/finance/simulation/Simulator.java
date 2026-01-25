package com.falizsh.finance.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.falizsh.finance.shared.utils.FinancialCalculous.calculateNtnbUnitPrice;

public class Simulator {



    public SimulationResult simularCenario(BigDecimal vnaAtual, BigDecimal taxaCompra, BigDecimal taxaSimulada, int diasUteisVencimento) {

        // 1. Calcula preço na taxa original (7%) - "Hoje"
        BigDecimal precoCompra = calculateNtnbUnitPrice(vnaAtual, taxaCompra, diasUteisVencimento);

        // 2. Calcula preço na taxa simulada (6%) - "Amanhã"
        // Obs: Em rigor, "amanhã" teria 1 dia útil a menos, mas para simulação rápida pode-se manter ou subtrair 1.
        BigDecimal precoVenda = calculateNtnbUnitPrice(vnaAtual, taxaSimulada, diasUteisVencimento);

        // 3. Resultado
        BigDecimal lucroBruto = precoVenda.subtract(precoCompra);
        BigDecimal rentabilidade = lucroBruto.divide(precoCompra, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

        return new SimulationResult(precoCompra, precoVenda, lucroBruto, rentabilidade);
    }

}
