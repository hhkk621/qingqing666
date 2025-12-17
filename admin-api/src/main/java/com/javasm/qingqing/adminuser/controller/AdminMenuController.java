package com.javasm.qingqing.adminuser.controller;

import com.javasm.qingqing.adminuser.service.MenuService;
import com.javasm.qingqing.common.exception.R;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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



}
