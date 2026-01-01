package com.falizsh.finance.shared.stock.adapter.impl;

import com.falizsh.finance.shared.stock.adapter.StockLookupService;
import com.falizsh.finance.shared.stock.adapter.YahooClient;
import com.falizsh.finance.shared.stock.adapter.dto.StockInfoDTO;
import com.falizsh.finance.shared.stock.adapter.dto.YahooChartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@RequiredArgsConstructor
@Service("yahooStockProvider")
public class YahooStockProvider implements StockLookupService {

    private final YahooClient yahooClient;

    @Override
    public StockInfoDTO findStockPrice(String ticker) {

        try {

            String formattedTicker = ticker.endsWith(".SA") ? ticker : ticker + ".SA";

            YahooChartResponse response = yahooClient.getChart(formattedTicker, "1d", "1d");

            if (
                    response != null &&
                    response.chart() != null &&
                    response.chart().result() != null &&
                    !response.chart().result().isEmpty()
            ) {

                var meta = response.chart().result().get(0).meta();

                if (meta == null || meta.regularMarketPrice() == null) {
                    return null;
                }

                BigDecimal previousClose = meta.chartPreviousClose();
                BigDecimal currentPrice = meta.regularMarketPrice();

                BigDecimal change = BigDecimal.ZERO;
                BigDecimal changePercent = BigDecimal.ZERO;

                if (previousClose != null && previousClose.compareTo(BigDecimal.ZERO) != 0) {
                    change = currentPrice.subtract(previousClose);
                    changePercent = change
                            .divide(previousClose, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
                }

                return new StockInfoDTO(
                        meta.symbol().replace(".SA", ""), // Remove .SA para padronizar
                        meta.name(),
                        currentPrice,
                        change,
                        changePercent,
                        null
                );

            }
            
            return null;

        } catch (Exception e) {
            log.error("Erro ao consultar Yahoo Finance (Chart API) para {}", ticker, e);
            return null;
        }

    }
}
