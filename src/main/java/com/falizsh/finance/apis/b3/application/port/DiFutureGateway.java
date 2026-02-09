package com.falizsh.finance.apis.b3.application.port;

import com.falizsh.finance.apis.b3.application.dto.DiFutureResult;

import java.util.List;

public interface DiFutureGateway {

    List<DiFutureResult> getSettlementPrices();

}
