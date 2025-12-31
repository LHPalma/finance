package com.falizsh.finance.bankAccount.adapter;

import com.falizsh.finance.bankAccount.adapter.dto.CurrencyInfoDTO;

public interface CurrencyLookupProvider {

    CurrencyInfoDTO findQuotations(String currencyPair);

    int getPriorityOrder();

    String getProviderName();

}
