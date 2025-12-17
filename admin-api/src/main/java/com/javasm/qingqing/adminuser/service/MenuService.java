package com.javasm.qingqing.adminuser.service;

import com.javasm.qingqing.adminuser.entity.AdminMenu;

import java.util.List;

public interface MenuService {
    List<AdminMenu> listAll(Integer rid);
}
