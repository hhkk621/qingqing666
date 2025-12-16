package com.javasm.qingqing.adminuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.adminuser.dao.AdminMenuDao;
import com.javasm.qingqing.adminuser.entity.AdminMenu;
import com.javasm.qingqing.adminuser.service.AdminMenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (AdminMenu)表服务实现类
 *
 * @author makejava
 * @since 2025-12-16 09:54:46
 */
@Service("adminMenuService")
public class AdminMenuServiceImpl extends ServiceImpl<AdminMenuDao, AdminMenu> implements AdminMenuService {
    @Resource
    AdminMenuDao adminMenuDao;

    @Override
    public List<AdminMenu> getListByRid(Integer rid) {
        List<AdminMenu> allList = adminMenuDao.selectListByRid(rid);
        // 使用stream流操作将allList按照父子级分离
        // 先按pid分组
        Map<Integer, List<AdminMenu>> menuMap = allList.stream()
                .collect(Collectors.groupingBy(AdminMenu::getPid));

        // 获取一级菜单（pid为-1的菜单）
        List<AdminMenu> oneList = menuMap.getOrDefault(-1, List.of());

        // 为每个一级菜单设置子菜单列表
        oneList.forEach(one -> {
            one.setChildList(menuMap.getOrDefault(one.getMid(), List.of()));
        });

        return oneList;
    }
}

