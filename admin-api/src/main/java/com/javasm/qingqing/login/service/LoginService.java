package com.javasm.qingqing.login.service;

import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.login.entity.LoginUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

/**
 * @className: LoginService
 * @author: gfs
 * @date: 2025/12/15 11:15
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface LoginService {
    void sendBackPasswordEmail(String email);

    AdminUser checkEmail(String token);

    AdminUser doJsonLogin(AdminUser adminUser);

    String doUploadHead(MultipartFile file);

    AdminUser getLoginUser();
}
