package com.javasm.qingqing.adminuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.adminuser.entity.AdminMenu;

import java.util.List;

/**
 * (AdminMenu)表服务接口
 *
 * @author makejava
 * @since 2025-12-16 09:54:46
 */
public interface AdminMenuService extends IService<AdminMenu> {
    List<AdminMenu> getListByRid(Integer rid);

    List<AdminMenu> listByRoleId(Integer roleId);
}

