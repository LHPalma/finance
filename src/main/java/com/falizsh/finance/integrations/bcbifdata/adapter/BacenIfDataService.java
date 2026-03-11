package com.falizsh.finance.integrations.bcbifdata.adapter;

import com.falizsh.finance.integrations.bcbifdata.dto.BaselIndexResponse;

public interface BacenIfDataService {

    BaselIndexResponse fetchBaselIndex(String institution, String referenceDate);

}
