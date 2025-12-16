package com.javasm.qingqing.adminuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.adminuser.dao.AdminUserDao;
import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.service.AdminRoleService;
import com.javasm.qingqing.adminuser.service.AdminUserService;
import com.javasm.qingqing.common.container.RedisKeys;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (AdminUser)表服务实现类
 *
 * @author makejava
 * @since 2025-12-15 11:18:48
 */
@Service("adminUserService")
public class AdminUserServiceImpl extends ServiceImpl<AdminUserDao, AdminUser> implements AdminUserService {
    @Resource
    AdminRoleService roleService;
    @Resource
    RedisTemplate<String,AdminUser> redisTemplate;
    @Override
    public AdminUser getByEmail(String email) {
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getEmail,email);
        return getOne(queryWrapper);
    }

    @Override
    public AdminUser getByUsername(String username) {
        //String AdminUserLoginUname = "adminuser:login:uname:%s";
        String key = String.format(RedisKeys.AdminUserLoginUname,username);
        AdminUser redisUser = redisTemplate.opsForValue().get(key);
        if (redisUser != null){
            return redisUser;
        }
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername,username);
        AdminUser adminUser = getOne(queryWrapper);
        if (adminUser != null){
            AdminRole role = roleService.getByRid(adminUser.getRoleId().intValue());
            adminUser.setRole(role);
            redisTemplate.opsForValue().set(key,adminUser,15, TimeUnit.DAYS);
        }else {
            redisTemplate.opsForValue().set(key,new AdminUser(),10,TimeUnit.MINUTES);
        }

        return adminUser;
    }
}

