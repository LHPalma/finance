package com.falizsh.finance.integrations.anbima.adapter;


import com.falizsh.finance.infrastructure.config.feign.FeignNativeConfig;
import feign.Headers;
import feign.RequestLine;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "anbimaClient",
        url = "${anbima.base-url}",
        configuration = FeignNativeConfig.class
)
public interface AnbimaClient {

    @RequestLine("POST /informacoes/vna/vna-down.asp")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Response downloadVnaXml(String body);

}
