package com.falizsh.finance.marketdata.bcbifdata.adapter;

import com.falizsh.finance.marketdata.bcbifdata.dto.BaselIndexResponse;

public interface BacenIfDataService {

    BaselIndexResponse fetchBaselIndex(String institution, String referenceDate);

}
