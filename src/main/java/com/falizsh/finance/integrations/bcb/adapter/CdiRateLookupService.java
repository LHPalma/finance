package com.falizsh.finance.integrations.bcb.adapter;

import com.falizsh.finance.integrations.bcb.adapter.dto.CdiRateInfoDTO;

public interface CdiRateLookupService {

    CdiRateInfoDTO findLatestDailyRate();

}
