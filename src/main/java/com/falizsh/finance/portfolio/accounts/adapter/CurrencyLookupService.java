package com.falizsh.finance.portfolio.accounts.adapter;

import com.falizsh.finance.portfolio.accounts.adapter.dto.CurrencyInfoDTO;

public interface CurrencyLookupService {

    CurrencyInfoDTO findExchangeRate(String from, String to);

}
