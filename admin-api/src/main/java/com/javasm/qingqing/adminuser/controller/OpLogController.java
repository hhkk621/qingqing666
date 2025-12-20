package com.javasm.qingqing.adminuser.controller;

import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.entity.SysLog;
import com.javasm.qingqing.adminuser.service.SysLogService;
import com.javasm.qingqing.adminuser.vo.LogQueryVo;
import com.javasm.qingqing.adminuser.vo.SearchVo;
import com.javasm.qingqing.annotation.OperateContent;
import com.javasm.qingqing.annotation.OperateModule;
import com.javasm.qingqing.common.exception.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@OperateModule("操作日志")
@RestController
@RequestMapping("/admin/log")
public class OpLogController {

    @Resource
    private SysLogService sysLogService;

    //分页条件查询日
    @RequestMapping("/page")
    @OperateContent("分页条件查询日志")
    public R page(LogQueryVo logQueryVo,
                  @RequestParam(defaultValue = "1") Integer pageNum,
                  @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<SysLog> pageInfo = sysLogService.page(logQueryVo, pageNum, pageSize);
        return R.ok(pageInfo);
    }
}
