package com.javasm.qingqing.news.controller;

import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.news.service.NewsService;
import com.javasm.qingqing.news.vo.NewsVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @className: NewsController
 * @author: gfs
 * @date: 2025/12/3 14:52
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    NewsService newsService;

    //同步新闻列表
    @GetMapping("/sync")
    public R syncNewsList(){
        newsService.syncNewsList();
        return R.ok();
    }

    @GetMapping("/list")
    public R queryNewsList(){
        List<NewsVo> newsList = newsService.list();
        return R.ok(newsList);
    }
}
