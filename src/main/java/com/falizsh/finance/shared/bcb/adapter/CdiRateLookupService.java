package com.falizsh.finance.shared.bcb.adapter;

import com.falizsh.finance.shared.bcb.adapter.dto.CdiRateInfoDTO;

public interface CdiRateLookupService {

    CdiRateInfoDTO findLatestDailyRate();

}
