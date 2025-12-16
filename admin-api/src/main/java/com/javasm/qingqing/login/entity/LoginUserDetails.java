package com.javasm.qingqing.login.entity;

import com.javasm.qingqing.adminuser.entity.AdminMenu;
import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.entity.AdminUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@Getter
public class LoginUserDetails implements UserDetails {

    private AdminUser adminUser;

    public LoginUserDetails(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        //根据角色授权
        AdminRole adminRole = adminUser.getRole();
        if (adminRole != null) {
            GrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + adminRole.getCode());
            list.add(authority);
        }
        //存储授权列表
        return list;
    }

    @Override
    public String getPassword() {
        //密码
        return adminUser.getPassword();
    }

    @Override
    public String getUsername() {
        //用户名
        return adminUser.getUsername();
    }

    //【一定记得把下方四个方法的返回值改成true】
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //isvalid =1有效 =0无效
        return adminUser.getIsvalid() == 1;
    }

    public boolean checkMenu(String url) {
        if (adminUser != null && adminUser.getRole() != null) {
            //获取菜单列表
            List<AdminMenu> menuList = adminUser.getRole().getMenuList();
            //获取子菜单的Stream流
            Stream<String> childUrlStream = menuList.stream()
                    .map(AdminMenu::getChildList)
                    .flatMap(Collection::stream)
                    .map(AdminMenu::getUrl);
            //获取父菜单的Stream流
            Stream<String> firstUrlStream = menuList.stream().map(AdminMenu::getUrl);
            //两个流混到一起，再筛选出url地址
            List<String> urlList = Stream.concat(childUrlStream, firstUrlStream).toList();
            return urlList.contains(url);
        }

        return false;
    }
}
