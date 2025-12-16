package com.javasm.qingqing.adminuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.adminuser.entity.AdminRole;

/**
 * (AdminRole)表服务接口
 *
 * @author makejava
 * @since 2025-12-16 09:54:36
 */
public interface AdminRoleService extends IService<AdminRole> {
    AdminRole getByRid(Integer uid);
}

