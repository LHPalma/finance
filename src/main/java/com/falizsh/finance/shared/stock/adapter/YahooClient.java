package com.falizsh.finance.shared.stock.adapter;

import com.falizsh.finance.shared.stock.adapter.dto.YahooChartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yahooClient", url = "https://query1.finance.yahoo.com")
public interface YahooClient {

    @GetMapping(value = "/v8/finance/chart/{ticker}", headers = {
            "User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Accept=*/*",
            "Connection=keep-alive"
    })
    YahooChartResponse getChart(
            @PathVariable String ticker,
            @RequestParam("interval") String interval,
            @RequestParam("range") String range
    );

}
