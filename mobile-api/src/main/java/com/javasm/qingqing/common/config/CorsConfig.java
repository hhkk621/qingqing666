package com.javasm.qingqing.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",createCorsConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration createCorsConfig() {
        //详细的配置跨域的规则
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //配置 允许哪些路径，访问
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:5173");
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedOrigin("http://192.168.0.151:5173");
        //配置允许头信息
        corsConfiguration.addAllowedHeader("*");
        //允许任何请求方式
        corsConfiguration.addAllowedMethod("*");
        //允许cookie，这个不允许的话，会导致session失效
        corsConfiguration.setAllowCredentials(true);
        //超时时间
        corsConfiguration.setMaxAge(5000L);
        return corsConfiguration;
    }
}
