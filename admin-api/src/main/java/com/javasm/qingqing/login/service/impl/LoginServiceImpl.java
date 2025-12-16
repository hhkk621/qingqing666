package com.javasm.qingqing.login.service.impl;

import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.service.AdminUserService;
import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.common.utils.JWTUtil;
import com.javasm.qingqing.common.utils.MailUtil;
import com.javasm.qingqing.common.utils.QiniuKodoUtil;
import com.javasm.qingqing.login.entity.LoginUserDetails;
import com.javasm.qingqing.login.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    MailUtil mailUtil;
    @Resource
    TemplateEngine templateEngine;

    @Resource
    AdminUserService adminUserService;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    HttpSession httpSession;
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;

    @Resource
    SecurityContextRepository securityContextRepository;
    @Resource
    ThreadPoolTaskExecutor taskExecutor;
    @Resource
    private RedisTemplate<String, AdminUser> redisTemplate;

    @Override
    public AdminUser doJsonLogin(AdminUser adminUser) {
       /* String code = adminUser.getCode();
        Object imgCode = httpSession.getAttribute("img_code");
        if (code.isEmpty() || imgCode == null ||
                imgCode.toString().isEmpty() || !code.equals(imgCode)) {
            throw new JavasmException(ExceptionEnum.Code_Error);
        }*/
        //获取用户名和密码
        String username = adminUser.getUsername();
        String password = adminUser.getPassword();
        //根据用户名 查询用户信息
        AdminUser loginAdminUser = adminUserService.getByUsername(username);
        if (loginAdminUser == null) {
            throw new JavasmException(ExceptionEnum.User_Not_Found);
        }
        //判断 密码是否正确
        if (!this.passwordEncoder.matches(password, loginAdminUser.getPassword())) {
            throw new JavasmException(ExceptionEnum.Password_Error);
        }
        UserDetails userDetails = new LoginUserDetails(loginAdminUser);
        //创建一个新的 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(
                        userDetails,
                        loginAdminUser.getPassword(),
                        userDetails.getAuthorities()
                );
        //登录成功的标志，存储到上下文对象中
        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
        strategy.getContext().setAuthentication(authenticated);
        securityContextRepository.saveContext(strategy.getContext(), request, response);
        return loginAdminUser;
    }

    @Override
    public String doUploadHead(MultipartFile file) {
        //给七牛云
        String url = QiniuKodoUtil.uploadAdminHeader(file);
        //更新用户信息
        AdminUser loginUser = getLoginUser();
        //更新数据库，多线程中操作
        taskExecutor.execute(()->{
            loginUser.setHeadImg(url);
            loginUser.updateById();
            //更新Redis
            String key = String.format(RedisKeys.AdminUserLoginUname,loginUser.getUsername());
            redisTemplate.delete(key);
        });
        return url;
    }
    @Override
    public AdminUser getLoginUser() {
        //查询已经登录的用户信息
        //获取Security上下文对象
        SecurityContext context = SecurityContextHolder.getContext();
        //从上下文对象中，获取用户信息。UsernamePasswordAuthenticationToken
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new JavasmException(ExceptionEnum.Not_Login);
        }
        //principal  未登录成功之前，在UsernamePasswordAuthenticationFilter中，赋值是用户名
        //登录成功之后，在DaoAuthenticationProvider中，赋值是UserDetails
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUserDetails) {
            LoginUserDetails loginUserDetails = (LoginUserDetails) principal;
            return loginUserDetails.getAdminUser();
        } else {
            throw new JavasmException(ExceptionEnum.Not_Login);
        }
    }
    @Override
    public void sendBackPasswordEmail(String email) {
        //根据email地址，查询用户信息
        AdminUser adminUser = adminUserService.getByEmail(email);
        if (adminUser != null) {
            //发送邮件
            //确认 发送的html是什么
            //从thymeleaf中获取html代码
            //import org.thymeleaf.context.Context;
            Context context = new Context();
            context.setVariable("username", adminUser.getNickname());
            //获取token字符串
            String token = JWTUtil.generateEmail(email);
            context.setVariable("token", token);
            //获取静态html   backpassword.html 页面 传入 值 context对象
            String html = templateEngine.process("backpassword", context);
            mailUtil.sendMail(email, "密码找回", html);
        }
    }

    @Override
    public AdminUser checkEmail(String token) {
        String email = JWTUtil.parseEmail(token);
        AdminUser adminUser = adminUserService.getByEmail(email);
        return adminUser;
    }

}
