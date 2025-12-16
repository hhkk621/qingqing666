package com.javasm.qingqing.news.vo;

import lombok.Data;

/**
 * @className: NewsResult
 * @author: gfs
 * @date: 2025/12/3 15:01
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class NewsResult {
    private Integer code;
    private String msg;
    private NewsPage result;
}
