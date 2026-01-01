package com.falizsh.finance.shared.stock.adapter.impl;

import com.falizsh.finance.shared.stock.adapter.BrapiClient;
import com.falizsh.finance.shared.stock.adapter.StockLookupService;
import com.falizsh.finance.shared.stock.adapter.dto.BrapiQuoteResponse;
import com.falizsh.finance.shared.stock.adapter.dto.StockInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("brapiStockProvider")
public class BrapiStockProvider implements StockLookupService {

    private final BrapiClient client;

    @Value("${brapi.token}")
    private String token;

    @Override
    public StockInfoDTO findStockPrice(String ticker) {

        try {

            BrapiQuoteResponse response = client.getQuote(ticker, token);

            if (response != null && response.results() != null && !response.results().isEmpty()) {
                var data = response.results().get(0);

                return new StockInfoDTO(
                        data.symbol(),
                        data.shortName(),
                        data.regularMarketPrice(),
                        data.change(),
                        data.changePercent(),
                        data.logourl()
                );

            }

            return null;

        } catch (Exception e) {
            log.error("Erro ao consultar ação na Brapi: {}", ticker, e);
            return null;
        }

    }
}
