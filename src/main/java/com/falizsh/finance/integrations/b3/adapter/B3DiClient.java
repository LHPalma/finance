package com.falizsh.finance.integrations.b3.adapter;

import com.falizsh.finance.integrations.b3.adapter.dto.B3DiResponse;
import com.falizsh.finance.infrastructure.config.feign.FeignNativeConfig;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "b3Client", url = "${b3.base-url}", configuration = FeignNativeConfig.class)
public interface B3DiClient {

    @RequestLine("GET /mds/api/v1/DerivativeQuotation/{ticker}")
    B3DiResponse getDerivativeQuotation(@Param("ticker") String ticker);

}
