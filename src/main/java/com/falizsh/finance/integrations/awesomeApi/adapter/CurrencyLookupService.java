package com.falizsh.finance.integrations.awesomeApi.adapter;

import com.falizsh.finance.integrations.awesomeApi.adapter.dto.CurrencyInfoDTO;

public interface CurrencyLookupService {

    CurrencyInfoDTO findExchangeRate(String from, String to);

}
