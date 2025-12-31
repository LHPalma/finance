package com.falizsh.finance.bankAccount.adapter;

import com.falizsh.finance.bankAccount.adapter.dto.CurrencyInfoDTO;

public interface CurrencyLookupService {

    CurrencyInfoDTO findExchangeRate(String from, String to);

}
