package com.javasm.qingqing.room.vo;

import lombok.Data;

/**
 * @className: Agreement
 * @author: gfs
 * @date: 2025/12/2 11:37
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class MessageVo {
    private Integer agreement;
    private Integer uid;
    private String pic;
    private String nickname;

    private String message;
}
