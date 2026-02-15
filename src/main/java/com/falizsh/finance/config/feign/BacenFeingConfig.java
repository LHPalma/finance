package com.falizsh.finance.config.feign;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class BacenFeingConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor oDataInterceptor() {
        return template -> {
            String url = template.url();
            if (url.contains("%24")) {
                template.uri(url.replace("%24", "$"));
            }
        };
    }
}