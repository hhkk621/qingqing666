package com.javasm.qingqing.common.aspect;

import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.webuser.entity.WebUser;
import com.javasm.qingqing.webuser.entity.WebUserLoginLog;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

/**
 * @className: WebLoginLogAspect
 * @author: gfs
 * @date: 2025/11/29 11:48
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Component
@Aspect
public class WebLoginLogAspect {

    @Resource
    RedisTemplate<String,Integer> redisTemplate;

    @AfterReturning(
            value = "@annotation(com.javasm.qingqing.common.annotation.WebLoginLog)",
            returning = "webUser"
    )
    @Async
    public void saveLog(JoinPoint joinPoint,Object webUser){
        if (webUser != null && webUser instanceof WebUser){
            //记录日志
            //记录日志的行为，不在主要流程里，
            // 日志是否记录以及是否记录成功，是否抛出异常，都不影响登录成功了
            WebUser user = (WebUser) webUser;
            Integer uid = user.getUid();
            //如何判断 今天是否已经记录过日志了
            //因为 登录接口访问的比较频繁，如果通过mysql判断数据是否已经存储了，会给数据库带来很大压力
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //key webuser:login:log:1006:2025-11-29
            String key = String.format(RedisKeys.WebLoginLog,uid,today);
            Integer redisUid = redisTemplate.opsForValue().get(key);
            if (redisUid == null){
                //今天还没有存储过
                WebUserLoginLog webUserLoginLog = new WebUserLoginLog();
                webUserLoginLog.setUid(uid);
                webUserLoginLog.setLoginTime(new Date());
                webUserLoginLog.insert();
                //redis中 记录今天登录信息
                redisTemplate.opsForValue().set(key,uid,1, TimeUnit.DAYS);
            }
            /*new Thread(()->{

            }).start();*/
        }
    }
}
