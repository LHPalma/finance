package com.falizsh.finance.shared.bcb.adapter;

import com.falizsh.finance.shared.bcb.adapter.dto.SelicRateInfoDTO;

public interface SelicRateLookupService {

    SelicRateInfoDTO findLatestDailyRate();

}
