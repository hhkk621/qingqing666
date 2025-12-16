package com.javasm.qingqing.adminuser.service;

import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.vo.SearchVo;

import java.util.List;

/**
 * @className: RoleService
 * @author: gfs
 * @date: 2025/12/16 11:28
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface RoleService {
    List<AdminRole> roleListAll();

    PageInfo<AdminRole> page(SearchVo searchVo, Integer pageNum, Integer pageSize);

    void saveOrUpdate(AdminRole role);
}
