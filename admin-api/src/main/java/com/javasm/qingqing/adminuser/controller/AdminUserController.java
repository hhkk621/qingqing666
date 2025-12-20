package com.javasm.qingqing.adminuser.controller;

import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.service.UserService;
import com.javasm.qingqing.adminuser.vo.SearchVo;
import com.javasm.qingqing.annotation.OperateContent;
import com.javasm.qingqing.annotation.OperateModule;
import com.javasm.qingqing.common.exception.R;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @className: AdminUserController
 * @author: gfs
 * @date: 2025/12/15 14:51
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@OperateModule("用户管理")
@RequestMapping("/admin/user")
@PreAuthorize("@menuAuth.check('/user/list')")
public class AdminUserController {
    /**
     * import org.apache.logging.log4j.LogManager;
     * import org.apache.logging.log4j.Logger;
     */
    Logger logger = LogManager.getLogger(AdminUserController.class);

    @GetMapping("/f1")
    public R f1(){
        logger.debug("-------------------我是Debug");
        logger.info("-------------------我是Info");
        logger.warn("-------------------我是警告");
        logger.error("-------------------我是Error!!!!");
        return R.ok();
    }

    @Resource
    UserService userService;

    @OperateContent("用户列表")
    @GetMapping("/page")
    public R page(SearchVo searchVo){

        PageInfo<AdminUser> pageInfo = userService.listByPage(searchVo);

        return R.ok(pageInfo);
    }

    @PostMapping("/save/update")
    public R saveOrUpdate(AdminUser adminUser){
        userService.saveOrUpdate(adminUser);
        return R.ok();
    }

    @DeleteMapping("/del/{ids}")
    public R deleteByIds(@PathVariable Integer[] ids){
        userService.deleteByIds(ids);
        return R.ok();
    }
}
