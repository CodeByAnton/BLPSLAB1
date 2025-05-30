package com.blpsteam.blpslab1.jca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YookassaConfig {

    @Value("${yookassa.shopId}")
    private String shopId;

    @Value("${yookassa.apiKey}")
    private String apiKey;

    @Bean
    public YookassaConnection yookassaConnection() {
        return new YookassaConnectionImpl(shopId, apiKey);
    }
}