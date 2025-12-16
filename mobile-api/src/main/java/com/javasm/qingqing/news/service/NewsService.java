package com.javasm.qingqing.news.service;

import com.javasm.qingqing.news.vo.NewsVo;

import java.util.List;

/**
 * @className: NewsService
 * @author: gfs
 * @date: 2025/12/3 14:53
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface NewsService {
    List<NewsVo> syncNewsList();

    List<NewsVo> list();
}
