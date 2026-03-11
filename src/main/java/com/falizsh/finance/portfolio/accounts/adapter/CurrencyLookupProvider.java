package com.falizsh.finance.portfolio.accounts.adapter;

import com.falizsh.finance.portfolio.accounts.adapter.dto.CurrencyInfoDTO;

public interface CurrencyLookupProvider {

    CurrencyInfoDTO findQuotations(String currencyPair);

    int getPriorityOrder();

    String getProviderName();

}
