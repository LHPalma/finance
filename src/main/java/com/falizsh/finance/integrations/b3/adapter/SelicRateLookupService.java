package com.falizsh.finance.integrations.b3.adapter;

import com.falizsh.finance.integrations.b3.adapter.dto.SelicRateInfoDTO;

public interface SelicRateLookupService {

    SelicRateInfoDTO findLatestDailyRate();

}
