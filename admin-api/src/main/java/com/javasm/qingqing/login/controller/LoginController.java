package com.javasm.qingqing.login.controller;

import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.common.utils.ParameterUtils;
import com.javasm.qingqing.login.entity.LoginUserDetails;
import com.javasm.qingqing.login.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @className: LoginController
 * @author: gfs
 * @date: 2025/12/15 11:15
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    LoginService loginService;

    @GetMapping("/password/back")
    public R backPassword(String email){
        loginService.sendBackPasswordEmail(email);
        return R.ok();
    }

    @GetMapping("/checkemail")
    public R checkEmail(@RequestParam("t") String token){
        AdminUser adminUser = loginService.checkEmail(token);
        return R.ok(adminUser);
    }

    @PostMapping("/json")
    public R doJsonLogin(@RequestBody AdminUser adminUser){
        ParameterUtils.checkParameter(adminUser);
        ParameterUtils.checkParameter(adminUser.getUsername(),adminUser.getPassword());
        AdminUser loginUser = loginService.doJsonLogin(adminUser);
        return R.ok(loginUser);
    }

    @PostMapping("/doUnameLogin")
    public R doUnameLogin(String username,String password){
        ParameterUtils.checkParameter(username,password);
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        AdminUser loginUser = loginService.doJsonLogin(adminUser);
        return R.ok(loginUser);
    }

    @PostMapping("/doUploadPic")
    public R updateHead(MultipartFile file){
        String url = loginService.doUploadHead(file);
        return R.ok(url);
    }

    @GetMapping("/my")
    public R queryMyUser(){
        AdminUser loginUserDetails = loginService.getLoginUser();
        return R.ok(loginUserDetails);
    }
}
