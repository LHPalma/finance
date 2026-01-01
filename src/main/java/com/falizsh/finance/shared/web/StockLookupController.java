package com.falizsh.finance.shared.web;

import com.falizsh.finance.shared.brapiAPIAdapter.StockLookupService;
import com.falizsh.finance.shared.brapiAPIAdapter.dto.StockInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/finance/utils/stock")
@RequiredArgsConstructor
public class StockLookupController {

    private final StockLookupService stockService;

    @GetMapping("/{ticker}")
    public ResponseEntity<StockInfoDTO> getStockPrice(@PathVariable String ticker) {
        StockInfoDTO stock = stockService.findStockPrice(ticker.toUpperCase());

        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(stock);
    }
}