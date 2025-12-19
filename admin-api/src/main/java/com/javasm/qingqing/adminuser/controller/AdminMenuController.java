package com.javasm.qingqing.adminuser.controller;

import com.javasm.qingqing.adminuser.entity.AdminMenu;
import com.javasm.qingqing.adminuser.service.MenuService;
import com.javasm.qingqing.common.exception.R;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/menu")
@PreAuthorize("@menuAuth.check('/menu/list')")
public class AdminMenuController {

    @Resource
    MenuService menuService;

    @RequestMapping("/list")
    public R list(@RequestParam(defaultValue = "-1") Integer rid){
        return R.ok(menuService.listAll(rid));
    }

    //删除
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable Integer[] ids){
        menuService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/saveOrUpdate")
    public R saveOrUpdate(@RequestBody AdminMenu menu){
        menuService.saveOrUpdate(menu);
        return R.ok();
    }

    @PutMapping("/changeStatus")
    public R changeStatus(Integer mid, Integer status){
        menuService.changeStatus(mid,status);
        return R.ok();
    }

    //得到一级菜单
    @GetMapping("/getOneLevelMenu")
    public R getOneLevelMenu(){
        return R.ok(menuService.getOneLevelMenu());
    }


}
