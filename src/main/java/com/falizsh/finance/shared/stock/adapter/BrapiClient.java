package com.falizsh.finance.shared.stock.adapter;


import com.falizsh.finance.shared.stock.adapter.dto.BrapiQuoteResponse;
import com.falizsh.finance.shared.stock.adapter.dto.BrapiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "brapiClient", url = "${brapi.url:https://brapi.dev/api}")
public interface BrapiClient {

    @GetMapping("v2/currency")
    BrapiResponse getCurrency(
            @RequestParam("currency") String currencyPair,
            @RequestParam("token") String token
    );

    @GetMapping("/quote/{tickers}")
    BrapiQuoteResponse getQuote(
            @PathVariable String tickers,
            @RequestParam("token") String token
    );

}
