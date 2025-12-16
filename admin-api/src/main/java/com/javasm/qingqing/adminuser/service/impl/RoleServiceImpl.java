package com.javasm.qingqing.adminuser.service.impl;

import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.service.AdminRoleService;
import com.javasm.qingqing.adminuser.service.RoleService;
import com.javasm.qingqing.common.container.RedisKeys;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @className: RoleServiceImpl
 * @author: gfs
 * @date: 2025/12/16 11:28
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Service("myRoleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    AdminRoleService adminRoleService;
    //建议把role的集合放到redis中
    //因为数据量太小，可以存储到JVM内存中
    private static List<AdminRole> roleList = new ArrayList<>();
    private static Long roleListExpTime = 1000L * 60 * 60 * 2;
    private static Long roleListSaveTime = 0L;

    @Override
    public List<AdminRole> roleListAll() {
        //判断 roleList是否过期
        if (System.currentTimeMillis() - roleListSaveTime > roleListExpTime){
            roleList = new ArrayList<>();
        }
        if (roleList.isEmpty()){
            roleList = adminRoleService.list();
            roleListSaveTime = System.currentTimeMillis();
        }
        return roleList;
    }
}
