package com.falizsh.finance.bankAccount.adapter;

import com.falizsh.finance.bankAccount.adapter.dto.CurrencyInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "awesomeApi", url = "https://economia.awesomeapi.com.br")
public interface AwesomeApiClient {

    @GetMapping("/last/USD-BRL")
    Map<String, CurrencyInfoDTO> getUsdToBrl();

    @GetMapping("/last/{coins}")
    Map<String, CurrencyInfoDTO> getQuotations(@PathVariable String coins);

}
