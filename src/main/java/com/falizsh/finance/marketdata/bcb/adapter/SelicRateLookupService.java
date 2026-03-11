package com.falizsh.finance.marketdata.bcb.adapter;

import com.falizsh.finance.marketdata.bcb.adapter.dto.SelicRateInfoDTO;

public interface SelicRateLookupService {

    SelicRateInfoDTO findLatestDailyRate();

}
