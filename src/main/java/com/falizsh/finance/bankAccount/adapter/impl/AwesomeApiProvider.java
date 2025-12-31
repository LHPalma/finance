package com.falizsh.finance.bankAccount.adapter.impl;

import com.falizsh.finance.bankAccount.adapter.AwesomeApiClient;
import com.falizsh.finance.bankAccount.adapter.CurrencyLookupProvider;
import com.falizsh.finance.bankAccount.adapter.dto.CurrencyInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwesomeApiProvider implements CurrencyLookupProvider {

    private final AwesomeApiClient client;

    @Override
    public CurrencyInfoDTO findQuotations(String currencyPair) {
        try {

            Map<String, CurrencyInfoDTO> response = client.getQuotations(currencyPair);

            String responseKey = currencyPair.replace("-", "");

            if (response!= null && response.containsKey(responseKey)) {
                return response.get(responseKey);
            }

            log.warn("Cotação não encontrada na resposta da AwesomeAPI para: {}", currencyPair);
            return null;

        } catch (Exception e) {
            log.error("Erro ao consultar AwesomeAPI", e);
            return null;
        }
    }

    @Override
    public int getPriorityOrder() {
        return 1;
    }

    @Override
    public String getProviderName() {
        return "AwesomeAPI";
    }
}
