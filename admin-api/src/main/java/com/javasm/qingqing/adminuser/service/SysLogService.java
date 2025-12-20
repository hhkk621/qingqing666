package com.javasm.qingqing.adminuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.SysLog;
import com.javasm.qingqing.adminuser.vo.LogQueryVo;
import com.javasm.qingqing.adminuser.vo.SearchVo;

/**
 * 系统日志表(SysLog)表服务接口
 *
 * @author makejava
 * @since 2025-12-20 15:13:48
 */
public interface SysLogService extends IService<SysLog> {

    PageInfo<SysLog> page(LogQueryVo logQueryVo, Integer pageNum, Integer pageSize);
}

