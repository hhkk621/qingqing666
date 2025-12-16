package com.javasm.qingqing.adminuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.adminuser.dao.AdminRoleDao;
import com.javasm.qingqing.adminuser.entity.AdminMenu;
import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.service.AdminMenuService;
import com.javasm.qingqing.adminuser.service.AdminRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (AdminRole)表服务实现类
 *
 * @author makejava
 * @since 2025-12-16 09:54:36
 */
@Service("adminRoleService")
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleDao, AdminRole> implements AdminRoleService {
    @Resource
    AdminMenuService adminMenuService;

    @Override
    public AdminRole getByRid(Integer uid) {
        AdminRole adminRole = getById(uid);
        if (adminRole != null){
            //查询菜单的信息
            List<AdminMenu> menuList =  adminMenuService.getListByRid(adminRole.getRid());
            adminRole.setMenuList(menuList);
        }
        return adminRole;
    }
}

