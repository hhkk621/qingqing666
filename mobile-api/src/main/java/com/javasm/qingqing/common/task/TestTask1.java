package com.javasm.qingqing.common.task;

import com.javasm.qingqing.news.service.NewsService;
import com.javasm.qingqing.news.vo.NewsVo;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @className: TestTask1
 * @author: gfs
 * @date: 2025/12/8 10:07
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
//@Component
public class TestTask1 {

    @Resource
    NewsService newsService;

    //cron 表达式，你希望当前的方法，每隔多久执行1次
    //秒 分 小时 日期 月份 星期 年（可以省略不写）
    @Scheduled(cron = "0/10 * * * * ?")
    @Async
    public void f1(){
        System.out.println("------------------------------------------");
        //List<NewsVo> list = newsService.list();
        //System.out.println(list);
    }
    //每秒钟执行1次，在每分钟的第10秒到第20秒执行
    @Scheduled(cron = "10-20 * * * * ?")
    @Async
    public void f2(){
        System.out.println("================================");
    }
}
