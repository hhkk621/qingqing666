package com.javasm.qingqing.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @className: WebSocketConfig
 * @author: gfs
 * @date: 2025/12/2 10:59
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Configuration
public class WebSocketConfig {
    //Bean注册，自动注册@ServerEndpoint注解的生命
    //Web socket Endpoint  端点
    //让端点注册成为Bean
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
