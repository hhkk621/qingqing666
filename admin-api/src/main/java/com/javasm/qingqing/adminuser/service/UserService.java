package com.javasm.qingqing.adminuser.service;

import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.vo.SearchVo;

/**
 * @className: userService
 * @author: gfs
 * @date: 2025/12/16 10:52
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface UserService {
    PageInfo<AdminUser> listByPage(SearchVo searchVo);

    void saveOrUpdate(AdminUser adminUser);

    void deleteByIds(Integer[] ids);
}
