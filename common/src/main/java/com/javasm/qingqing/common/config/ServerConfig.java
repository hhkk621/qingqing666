package com.javasm.qingqing.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @className: ServerConfig
 * @author: gfs
 * @date: 2025/11/29 15:21
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class ServerConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
