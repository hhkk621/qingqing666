package com.javasm.qingqing.login.service;

import com.javasm.qingqing.webuser.entity.WebUser;

/**
 * @className: LoginService
 * @author: gfs
 * @date: 2025/11/29 11:08
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface LoginService {
    WebUser doUnameLogin(String username, String password);

    WebUser doRegister(WebUser webUser);

    WebUser doUidLogin(String uid);
}
