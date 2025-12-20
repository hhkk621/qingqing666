package com.javasm.qingqing.aspect;

import com.javasm.qingqing.adminuser.entity.SysLog;
import com.javasm.qingqing.adminuser.service.SysLogService;
import com.javasm.qingqing.annotation.OperateContent;
import com.javasm.qingqing.annotation.OperateModule;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class SysLogAspect {

    @Resource
    private SysLogService sysLogService;


    /**
     * 定义切点：拦截controller包及子包下的所有方法
     */
    @Pointcut("execution(* com.javasm.qingqing.*.controller..*.*(..))")
    public void controllerPointcut() {}

    /**
     * 环绕通知：方法执行前后收集日志信息并保存
     */
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取HTTP请求（用于提取URL、请求方式等）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 2. 记录方法开始时间
        long start = System.currentTimeMillis();
        Date opTime = new Date(); // 操作时间

        // 3. 执行目标方法（Controller业务逻辑）
        Object result = null;
        String exceptionMsg = null; // 记录异常信息
        try {
            result = joinPoint.proceed(); // 执行Controller方法
            return result;
        } catch (Throwable e) {
            exceptionMsg = e.getMessage(); // 捕获异常信息
            throw e; // 抛出异常，保证业务逻辑正常报错
        } finally {
            // 4. 计算方法耗时
            long end = System.currentTimeMillis();
            long costTime = end - start;

            // 5. 封装SysLog实体（日志信息）
            SysLog sysLog = new SysLog();
            sysLog.setOpTime(opTime);
            sysLog.setUsername(getUsername()); // 从Spring Security获取当前用户
            sysLog.setModule(getModule(joinPoint)); // 操作模块（自定义注解或默认逻辑）
            sysLog.setOpContent(getOpContent(joinPoint));
            // 6. 保存日志到数据库（通过MyBatis Plus的Service）
            sysLogService.save(sysLog);
        }
    }

    private String getOpContent(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 1. 尝试读取自定义注解@OperateContent
        OperateContent operateContent = method.getAnnotation(OperateContent.class);
        if (operateContent != null) {
            return operateContent.value();
        }

        // 2. 无注解时，使用方法名作为操作内容
        return method.getName();
    }

    /**
     * 从Spring Security上下文获取当前登录用户名
     */
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return "匿名用户"; // 未登录场景
    }

    /**
     * 获取操作模块（优先用自定义注解@OperateModule，无注解则默认类名+方法名）
     */
    private String getModule(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // 1. 尝试读取方法上的@OperateModule注解
        OperateModule methodAnnotation = method.getAnnotation(OperateModule.class);
        if (methodAnnotation != null) {
            return methodAnnotation.value();
        }

        // 2. 尝试读取类上的@OperateModule注解
        OperateModule classAnnotation = targetClass.getAnnotation(OperateModule.class);
        if (classAnnotation != null) {
            return classAnnotation.value();
        }

        // 3. 无注解时，使用类名（去掉Controller后缀）作为模块
        String className = targetClass.getSimpleName();
        if (className.endsWith("Controller")) {
            className = className.substring(0, className.length() - "Controller".length());
        }
        return className;
    }

}
