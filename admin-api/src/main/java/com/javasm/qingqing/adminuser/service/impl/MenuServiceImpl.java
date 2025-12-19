package com.javasm.qingqing.adminuser.service.impl;

import com.javasm.qingqing.adminuser.entity.AdminMenu;
import com.javasm.qingqing.adminuser.service.AdminMenuService;
import com.javasm.qingqing.adminuser.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    AdminMenuService adminMenuService;

    @Override
    public List<AdminMenu> listAll(Integer rid) {
        return adminMenuService.listByRoleId(rid);

    }

    @Override
    public void deleteByIds(Integer... ids) {
        adminMenuService.removeByIds(Arrays.asList(ids));
    }


    public void saveOrUpdate(AdminMenu adminMenu) {
        if (adminMenu.getMid() == null) {
            //新增

            //先找到所有的一级菜单
            List<AdminMenu> menuList = listAll(-1);
            if (adminMenu.getPid() == -1) {
                //找到一级菜单最大的mid
                AdminMenu maxMenu = menuList.stream().max(Comparator.comparingInt(AdminMenu::getMid)).get();
                //然后给新的父菜单的mid设置一下
                adminMenu.setMid(maxMenu.getMid() + 1);
            } else {
                AdminMenu parentMenu = menuList.stream().filter(menu -> menu.getMid().equals(adminMenu.getPid())).findFirst().get();
                if (parentMenu.getChildList() == null || parentMenu.getChildList().isEmpty()) {
                    adminMenu.setMid(parentMenu.getMid() * 1000 + 1);
                } else {
                    parentMenu.getChildList().sort(Comparator.comparingInt(AdminMenu::getMid));
                    AdminMenu maxMenu = parentMenu.getChildList().get(0);
                    adminMenu.setMid(maxMenu.getMid() + 1);
                }
            }
            adminMenuService.save(adminMenu);
        } else {
            //修改
            adminMenuService.updateById(adminMenu);
        }

    }

    @Override
    public void changeStatus(Integer mid, Integer status) {
        AdminMenu menu = new AdminMenu();
        menu.setMid(mid);
        menu.setStatus(status);
        adminMenuService.updateById(menu);
    }

    @Override
    public Object getOneLevelMenu() {
        return adminMenuService.listAll(-1);
    }
}
