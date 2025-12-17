package com.javasm.qingqing.adminuser.service.impl;

import com.javasm.qingqing.adminuser.entity.AdminMenu;
import com.javasm.qingqing.adminuser.service.AdminMenuService;
import com.javasm.qingqing.adminuser.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    AdminMenuService adminMenuService;

    @Override
    public List<AdminMenu> listAll(Integer rid) {
        List<AdminMenu> menuList = adminMenuService.listByRoleId(rid);
        return menuList;
    }
}
