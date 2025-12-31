package com.falizsh.finance.bankAccount.adapter;

import com.falizsh.finance.bankAccount.adapter.dto.CurrencyInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class CurrencyLookupOrchestrator implements CurrencyLookupService {

    private final List<CurrencyLookupProvider> providers;

    @Override
    public CurrencyInfoDTO findExchangeRate(String from, String to) {
        String pair = from + "-" + to;

        return providers.stream()
                .sorted(Comparator.comparingInt(CurrencyLookupProvider::getPriorityOrder))
                .map(providers -> {
                    return providers.findQuotations(pair);
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> {
                    return null;
                });
    }
}
