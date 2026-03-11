package com.falizsh.finance.marketdata.bcb.adapter;

import com.falizsh.finance.marketdata.bcb.adapter.dto.CdiRateInfoDTO;

public interface CdiRateLookupService {

    CdiRateInfoDTO findLatestDailyRate();

}
