package com.javasm.qingqing.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.task.dao.QqTaskDao;
import com.javasm.qingqing.task.entity.QqTask;
import com.javasm.qingqing.task.service.QqTaskService;
import org.springframework.stereotype.Service;

/**
 * (QqTask)表服务实现类
 *
 * @author makejava
 * @since 2025-12-08 10:38:12
 */
@Service("qqTaskService")
public class QqTaskServiceImpl extends ServiceImpl<QqTaskDao, QqTask> implements QqTaskService {

}

