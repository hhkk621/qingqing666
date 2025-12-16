package com.javasm.qingqing.common.aspect;

import com.javasm.qingqing.common.utils.JWTUtil;
import com.javasm.qingqing.webuser.entity.WebUser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class SaveTokenAspect {

    @Resource
    HttpServletResponse response;

    @AfterReturning(
            value = "@annotation(com.javasm.qingqing.common.annotation.SaveToken)",
            returning = "webUser"
    )
    public void f1(JoinPoint joinPoint,Object webUser){
        if (webUser instanceof WebUser){
            WebUser loginUser = (WebUser) webUser;
            //获取uid
            Integer uid = loginUser.getUid();
            //生成Token字符串
            String token = JWTUtil.generateUid(uid.toString());
            //把token加入Header对象
            response.addHeader(JWTUtil.Server_Token,token);
            //默认情况下，前端js是不会随便处理别人给的Header数据的
            //需要额外配置一下，让js来处理自定义header
            response.setHeader("Access-Control-Expose-Headers",JWTUtil.Server_Token);
        }
    }
}
