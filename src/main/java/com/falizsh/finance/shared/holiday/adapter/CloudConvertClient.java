package com.falizsh.finance.shared.holiday.adapter;

import com.falizsh.finance.config.feign.FeignNativeConfig;
import com.falizsh.finance.shared.holiday.adapter.dto.CloudConvertJobRequest;
import com.falizsh.finance.shared.holiday.adapter.dto.CloudConvertJobResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.net.URI;

@FeignClient(
        name = "cloudConvertClient",
        url = "${cloudconvert.url}",
        configuration = FeignNativeConfig.class
)
public interface CloudConvertClient {

    @RequestLine("POST /v2/jobs")
    @Headers({
            "Content-Type: application/json",
            "Authorization: {token}"
    })
    CloudConvertJobResponse createJob(
            @Param("token") String token,
            CloudConvertJobRequest request
    );

    @RequestLine("GET /v2/jobs/{id}")
    @Headers("Authorization: {token}")
    CloudConvertJobResponse getJob(
            @Param("token") String token,
            @Param("id") String id
    );

    @RequestLine("GET")
    byte[] downloadFile (URI baseUri);
}
