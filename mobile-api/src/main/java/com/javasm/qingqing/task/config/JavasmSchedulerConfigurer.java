package com.javasm.qingqing.task.config;

import com.javasm.qingqing.task.entity.QqTask;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @className: JavasmSchedulerConfigurer
 * @author: gfs
 * @date: 2025/12/8 10:43
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Component
public class JavasmSchedulerConfigurer implements SchedulingConfigurer {


    ScheduledTaskRegistrar scheduledTaskRegistrar;

    private Map<Integer,ScheduledTask> taskMap = new ConcurrentHashMap<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //当前方法，是在项目启动的时候，执行
        //新建线程池，专门负责调用定时任务
        ScheduledExecutorService executorService =
                Executors.newScheduledThreadPool(10);
        ConcurrentTaskScheduler taskScheduler =
                new ConcurrentTaskScheduler(executorService);
        //开启注册 一个定时任务对象
        taskRegistrar.setScheduler(taskScheduler);
        //ScheduledTaskRegistrar taskRegistrar  参数是开启定时任务关键对象
        //当前方法只有在项目启动的时候执行，不能反复执行
        //写一个全局变量，来接收参数，方便随时调用
        this.scheduledTaskRegistrar = taskRegistrar;
    }

    //Spring容器对象
    @Resource
    ApplicationContext applicationContext;

    //注册定时任务的方法----开启定时任务
    public boolean regTask(QqTask task) {
        if (task == null){
            return false;
        }
        //获取任务id
        Integer id = task.getId();
        //获取执行的任务类com.javasm.qingqing.task.clazz.PrintLogTask
        String clazz = task.getClazz();
        //表达式
        String cron = task.getCron();
        //clazz 对应的对象怎么获取到
        try {
            //通过反射获取对象
            Class<?> aClass = Class.forName(clazz);
             /*Object o = aClass.getConstructor().newInstance();
            Runnable runnable = (Runnable) o;*/
            //通过Spring容器获取
            Object bean = applicationContext.getBean(aClass);
            Runnable runnable1 = (Runnable) bean;
            //创建任务对象
            CronTask cronTask = new CronTask(runnable1, cron);
            //开始定时任务
            ScheduledTask scheduledTask = this.scheduledTaskRegistrar.scheduleCronTask(cronTask);
            //停止定时任务
            //scheduledTask.cancel();
            taskMap.put(id,scheduledTask);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    //停止定时任务
    public boolean stop(Integer id){
        ScheduledTask scheduledTask = taskMap.get(id);
        if (scheduledTask != null){
            //停止定时任务
            scheduledTask.cancel();
            //从map中移除这个定时任务
            taskMap.remove(id);
            return true;
        }
        return false;
    }
}
