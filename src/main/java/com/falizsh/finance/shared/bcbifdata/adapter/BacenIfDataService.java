package com.falizsh.finance.shared.bcbifdata.adapter;

import com.falizsh.finance.shared.bcbifdata.dto.BaselIndexResponse;

public interface BacenIfDataService {

    BaselIndexResponse fetchBaselIndex(String institution, String referenceDate);

}
