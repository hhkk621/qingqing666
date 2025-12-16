package com.javasm.qingqing.adminuser.controller;

import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.service.RoleService;
import com.javasm.qingqing.adminuser.vo.SearchVo;
import com.javasm.qingqing.common.exception.R;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("admin/role")
@PreAuthorize("@menuAuth.check('/role/list')")
public class AdminRoleController {

    @Resource(name = "myRoleService")
    RoleService roleService;

    @GetMapping("/all")
    public R roleList(){
        List<AdminRole> list = roleService.roleListAll();
        return R.ok(list);
    }

    //分页查询所有的角色
    @GetMapping("/page")
    public R page(SearchVo searchVo,
                  @RequestParam(defaultValue = "1") Integer pageNum,
                  @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<AdminRole> pageInfo =roleService.page(searchVo, pageNum, pageSize);
        return R.ok(pageInfo);
    }

    //更新或添加角色
    @PostMapping("/saveOrUpdate")
    public R saveOrUpdate(@RequestBody AdminRole role){
        roleService.saveOrUpdate(role);
        return R.ok();
    }
}
