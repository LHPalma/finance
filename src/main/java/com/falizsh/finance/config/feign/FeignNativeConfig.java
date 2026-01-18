package com.falizsh.finance.config.feign;

import feign.Contract;
import org.springframework.context.annotation.Bean;

public class FeignNativeConfig {

    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }
}