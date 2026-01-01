package com.falizsh.finance.shared.stock.adapter.impl;

import com.falizsh.finance.shared.stock.adapter.StockLookupService;
import com.falizsh.finance.shared.stock.adapter.dto.StockInfoDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Primary
@Service
public class StockLookupOrchestrator implements StockLookupService {

    private final StockLookupService brapiProvider;
    private final StockLookupService yahooProvider;

    public StockLookupOrchestrator(
            @Qualifier("brapiStockProvider") StockLookupService brapiProvider,
            @Qualifier("yahooStockProvider") StockLookupService yahooProvider
    ) {
        this.brapiProvider = brapiProvider;
        this.yahooProvider = yahooProvider;
    }

    @Override
    @CircuitBreaker(name = "brapi-circuit-breaker", fallbackMethod = "fallBackToYahoo")
    public StockInfoDTO findStockPrice(String ticker) {
        StockInfoDTO result = brapiProvider.findStockPrice(ticker);

        if (result == null) {
            throw new RuntimeException("brapi.api.returned.null");
        }

        return result;
    }

    public StockInfoDTO fallBackToYahoo(String ticker, Throwable t) {
        log.warn("Circuito aberto ou erro na Brapi ({}). Acionando fallback Yahoo Finance para: {}", t.getMessage(), ticker);
        return yahooProvider.findStockPrice(ticker);
    }
}
