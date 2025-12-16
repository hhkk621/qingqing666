package com.javasm.qingqing.adminuser.controller;

import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.service.RoleService;
import com.javasm.qingqing.common.exception.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @className: AdminRoleController
 * @author: gfs
 * @date: 2025/12/16 11:27
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@RequestMapping("/role")
public class AdminRoleController {

    @Resource(name = "myRoleService")
    RoleService roleService;

    @GetMapping("/all")
    public R roleList(){
        List<AdminRole> list = roleService.roleListAll();
        return R.ok(list);
    }
}
