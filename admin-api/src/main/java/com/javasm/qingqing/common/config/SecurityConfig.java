package com.javasm.qingqing.common.config;

import com.alibaba.fastjson2.JSON;
import com.javasm.qingqing.common.exception.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //****************跨域--开始****************//
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");//允许所有的域名
        corsConfiguration.addAllowedMethod("*");//允许所有的请求头
        corsConfiguration.setAllowCredentials(true);//允许携带Cookie
        corsConfiguration.setMaxAge(3600L);//预检测请求缓冲时间
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //对所有的路径应用配置
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    //****************跨域--结束****************//
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.securityContext(context -> context.securityContextRepository(securityContextRepository()));
        //关闭csrf 跨域请求伪造的控制
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/login/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        );
        //  ↓配置表单提交

        //注销登录
        http.logout(logout -> logout
                .logoutUrl("/logout")
                //退出登录的时候，返回用户信息
                .logoutSuccessHandler((r, response, a) -> createSuccessJson(response, "退出登录成功！"))
                .permitAll()
        );
        http.exceptionHandling(ex -> ex
                //处理 401 未登录/未认证  （用户访问未携带cookie/未通过登录）
                .authenticationEntryPoint((request, response, e) -> {
                    createFailJson(response, "当前用户未登录，请先登录再访问");
                })
                //处理 403 已经登录，权限不足
                .accessDeniedHandler((request, response, e) -> {
                    createFailJson(response, "权限不足，无法访问");
                })
        );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        //指定加密算法
        return new BCryptPasswordEncoder();
    }

    private void createSuccessJson(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
        strategy.getContext().setAuthentication(authentication);
        securityContextRepository().saveContext(strategy.getContext(), request, response);
        createSuccessJson(response, authentication);
    }

    private void createSuccessJson(HttpServletResponse response, Object object) {
        R ok = R.ok(object);
        createJson(response, ok);
    }

    private void createFailJson(HttpServletResponse response, AuthenticationException e) {
        R error = R.error(e.getMessage());
        createJson(response, error);
    }

    private void createFailJson(HttpServletResponse response, String e) {
        R error = R.error(e);
        createJson(response, error);
    }

    private void createJson(HttpServletResponse response, R r) {
        try {
            response.setContentType("application/json;charset=utf-8");
            //如果想修改返回的状态码，可以这么修改
            //response.setStatus(r.getCode());
            //写出去
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(r));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
