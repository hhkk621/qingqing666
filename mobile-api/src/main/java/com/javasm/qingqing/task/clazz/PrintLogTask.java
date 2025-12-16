package com.javasm.qingqing.task.clazz;

import com.javasm.qingqing.news.service.NewsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PrintLogTask implements Runnable{

    @Resource
    NewsService newsService;
    @Override
    public void run() {
        System.out.println("-------输出测试日志----------");
    }
}
