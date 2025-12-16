package com.javasm.qingqing.news.vo;

import lombok.Data;

import java.util.List;

/**
 * @className: NewsPage
 * @author: gfs
 * @date: 2025/12/3 15:00
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class NewsPage {
    private Integer curpage;
    private Integer allnum;
    private List<NewsVo> newslist;
}
