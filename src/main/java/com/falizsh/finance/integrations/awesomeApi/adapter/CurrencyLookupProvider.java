package com.falizsh.finance.integrations.awesomeApi.adapter;

import com.falizsh.finance.integrations.awesomeApi.adapter.dto.CurrencyInfoDTO;

public interface CurrencyLookupProvider {

    CurrencyInfoDTO findQuotations(String currencyPair);

    int getPriorityOrder();

    String getProviderName();

}
