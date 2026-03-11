package com.falizsh.finance.integrations.b3.adapter;

import com.falizsh.finance.integrations.b3.adapter.dto.CdiRateInfoDTO;

public interface CdiRateLookupService {

    CdiRateInfoDTO findLatestDailyRate();

}
