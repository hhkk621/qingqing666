package com.javasm.qingqing.common.utils;

/**
 * @className: SpringContextUtil
 * @author: gfs
 * @date: 2025/12/2 11:01
 * @version: 0.1
 * @since: jdk17
 * @description:
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //在SpringBoot项目启动的时候执行，传入的参数是Spring的容器对象
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass){
        return context.getBean(beanClass);
    }
}
