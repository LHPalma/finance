package com.falizsh.finance.shared.web;

import com.falizsh.finance.bankAccount.adapter.CurrencyLookupService;
import com.falizsh.finance.bankAccount.adapter.dto.CurrencyInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/finance/utils/currency")
public class CurrencyLookupController {

    private final CurrencyLookupService currencyService;

    @GetMapping("/quote/{from}/{to}")
    public ResponseEntity<CurrencyInfoDTO> getQuote(
            @PathVariable String from,
            @PathVariable String to) {

        CurrencyInfoDTO quote = currencyService.findExchangeRate(from, to);

        if (quote == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(quote);
    }

}
