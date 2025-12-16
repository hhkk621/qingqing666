package com.javasm.qingqing.login.controller;

import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.common.utils.JWTUtil;
import com.javasm.qingqing.common.utils.ParameterUtils;
import com.javasm.qingqing.login.service.LoginService;
import com.javasm.qingqing.webuser.entity.WebUser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: LoginController
 * @author: gfs
 * @date: 2025/11/29 11:08
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    LoginService loginService;

    @PostMapping("/doUsernameLogin")
    public R doUnameLogin(String username, String password) {
        //检查参数合法性
        ParameterUtils.checkParameter(username, password);
        WebUser loginUser = loginService.doUnameLogin(username, password);
        return R.ok(loginUser);
    }
    @Resource
    HttpServletRequest request;

    @PostMapping("/auto")
    public R doUidLogin(){
        //获取token信息
        String token = request.getHeader(JWTUtil.Server_Token);
        if (token == null){
            throw new JavasmException(ExceptionEnum.Token_Expired_Error);
        }
        String uid = JWTUtil.parseUid(token);
        //代码执行到这里，没有抛出异常，说明已经是一个正常的token字符串了
        WebUser webUser = loginService.doUidLogin(uid);
        return R.ok(webUser);
    }

    @PostMapping("/doReg")
    public R doRegister(@RequestBody WebUser webUser) {
        //参加合法校验
        ParameterUtils.checkParameter(
                webUser,
                webUser.getUserInfo(),
                webUser.getUsername(),
                webUser.getUserInfo().getNickname()
        );
        WebUser regUser = loginService.doRegister(webUser);
        return R.ok(regUser);
    }
}
