package com.javasm.qingqing.common.interceptors;

import com.alibaba.druid.util.StringUtils;
import com.javasm.qingqing.common.annotation.Auth;
import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.common.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //默认，所有的接口，都能不登录就可以访问，只有加了@Auth的接口，才需要登录才能访问
        if (handler instanceof HandlerMethod){
            //说明 访问的是Controller的方法，而不是其他的地址
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //当前正在被访问的方法对象
            Method method = handlerMethod.getMethod();
            //判断，方法上面，是否有@Auth注解
            Auth auth = method.getAnnotation(Auth.class);
            if (auth == null){
                return true;
            }
            //代码执行到这里说明已经加了@Auth，所以必须登录才能访问
            //从Header中获取Token
            String token = request.getHeader(JWTUtil.Server_Token);
            if (StringUtils.isEmpty(token)){
                //如果没有传token，不让登录
                throw new JavasmException(ExceptionEnum.Token_Expired_Error);
            }
            //如果代码运行到这里，说明token不是null
            JWTUtil.parseUid(token);
            return true;
        }
        return true;
    }
}
