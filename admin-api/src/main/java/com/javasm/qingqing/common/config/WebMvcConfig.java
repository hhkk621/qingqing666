package com.javasm.qingqing.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @className: WebMvcConfig
 * @author: gfs
 * @date: 2025/12/10 15:38
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//允许所有的 路径
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);//预检测请求缓冲时间
    }
}
