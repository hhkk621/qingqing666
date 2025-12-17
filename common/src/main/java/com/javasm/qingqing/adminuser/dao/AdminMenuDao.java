package com.javasm.qingqing.adminuser.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javasm.qingqing.adminuser.entity.AdminMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (AdminMenu)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-16 09:54:46
 */
public interface AdminMenuDao extends BaseMapper<AdminMenu> {
    @Select("select menu.* from " +
            "admin_menu menu,rel_admin_role_menu rel " +
            "where menu.mid = rel.mid and " +
            "rel.rid = #{rid} ")
    List<AdminMenu> selectListByRid(Integer rid);


}

