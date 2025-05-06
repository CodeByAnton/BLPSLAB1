package com.blpsteam.blpslab1.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
public class JtaConfig {

    @Bean(name = "jtaTransactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        return new JtaTransactionManager();
    }
}