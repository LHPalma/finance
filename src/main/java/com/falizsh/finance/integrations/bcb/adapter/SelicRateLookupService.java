package com.falizsh.finance.integrations.bcb.adapter;

import com.falizsh.finance.integrations.bcb.adapter.dto.SelicRateInfoDTO;

public interface SelicRateLookupService {

    SelicRateInfoDTO findLatestDailyRate();

}
