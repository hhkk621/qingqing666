package com.javasm.qingqing.adminuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.adminuser.entity.AdminUser;

/**
 * (AdminUser)表服务接口
 *
 * @author makejava
 * @since 2025-12-15 11:18:48
 */
public interface AdminUserService extends IService<AdminUser> {

    AdminUser getByEmail(String email);

    AdminUser getByUsername(String username);
}

