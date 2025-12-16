package com.javasm.qingqing.common.autorize;


import com.javasm.qingqing.login.entity.LoginUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component("menuAuth")
public class MenuAuthorize {

    public boolean check(String url){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUserDetails){
            LoginUserDetails loginUserDetails = (LoginUserDetails) principal;
            return loginUserDetails.checkMenu(url);
        }
        return false;
    }
}
