package com.javasm.qingqing.adminuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.dao.SysLogDao;
import com.javasm.qingqing.adminuser.entity.SysLog;
import com.javasm.qingqing.adminuser.service.SysLogService;
import com.javasm.qingqing.adminuser.vo.LogQueryVo;
import com.javasm.qingqing.adminuser.vo.SearchVo;
import org.springframework.stereotype.Service;

/**
 * 系统日志表(SysLog)表服务实现类
 *
 * @author makejava
 * @since 2025-12-20 15:13:48
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

    @Override
    public PageInfo<SysLog> page(LogQueryVo logQueryVo, Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();
        if (logQueryVo != null) {
            queryWrapper.like(logQueryVo.getUsername() != null && !logQueryVo.getUsername().isEmpty(),
                    SysLog::getUsername, logQueryVo.getUsername());
            queryWrapper.like(logQueryVo.getModule() != null && !logQueryVo.getModule().isEmpty(),
                    SysLog::getModule, logQueryVo.getModule());
            queryWrapper.like(logQueryVo.getOpContent() != null && !logQueryVo.getOpContent().isEmpty(),
                    SysLog::getOpContent, logQueryVo.getOpContent());
            // 操作时间-开始：大于等于
            queryWrapper.ge(logQueryVo.getStartTime() != null,
                    SysLog::getOpTime, logQueryVo.getStartTime());

            // 操作时间-结束：小于等于
            queryWrapper.le(logQueryVo.getEndTime() != null,
                    SysLog::getOpTime, logQueryVo.getEndTime());

            // 按操作时间倒序（可选，根据需求调整排序）
            queryWrapper.orderByDesc(SysLog::getId);
        }

        return PageInfo.of(this.list(queryWrapper));
    }
}

