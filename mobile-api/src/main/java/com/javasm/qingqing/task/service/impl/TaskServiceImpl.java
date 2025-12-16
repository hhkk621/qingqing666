package com.javasm.qingqing.task.service.impl;

import com.javasm.qingqing.task.config.JavasmSchedulerConfigurer;
import com.javasm.qingqing.task.entity.QqTask;
import com.javasm.qingqing.task.service.QqTaskService;
import com.javasm.qingqing.task.service.TaskService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @className: TaskServiceImpl
 * @author: gfs
 * @date: 2025/12/8 10:39
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    JavasmSchedulerConfigurer schedulerConfigurer;
    @Resource
    QqTaskService qqTaskService;
    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void start(Integer id) {
        QqTask qqTask = qqTaskService.getById(id);
        if (schedulerConfigurer.regTask(qqTask)){
            taskExecutor.execute(()->{
                qqTask.setStatus(1);
                qqTask.updateById();
            });
        }
    }

    @Override
    public void stop(Integer id) {
        if (schedulerConfigurer.stop(id)){
            taskExecutor.execute(()->{
                QqTask task = new QqTask();
                task.setId(id);
                task.setStatus(0);
                task.updateById();
            });
        }

    }
}
