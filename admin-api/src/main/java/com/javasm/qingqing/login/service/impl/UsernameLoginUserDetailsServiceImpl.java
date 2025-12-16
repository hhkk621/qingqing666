package com.javasm.qingqing.login.service.impl;

import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.service.AdminUserService;
import com.javasm.qingqing.login.entity.LoginUserDetails;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("usernameLoginUserDetailsService")
public class UsernameLoginUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserService.getByUsername(username);
        if (adminUser != null){
            return new LoginUserDetails(adminUser);
        }
        return null;
    }
}
