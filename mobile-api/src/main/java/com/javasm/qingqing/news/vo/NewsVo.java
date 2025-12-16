package com.javasm.qingqing.news.vo;

import lombok.Data;

import java.util.Date;

/**
 * @className: NewsVo
 * @author: gfs
 * @date: 2025/12/3 14:59
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class NewsVo {
    private String id;
    private String ctime;
    private String title;
    private String description;
    private String source;
    private String picUrl;
    private String url;
}
