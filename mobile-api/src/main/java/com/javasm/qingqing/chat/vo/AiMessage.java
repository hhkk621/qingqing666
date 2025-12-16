package com.javasm.qingqing.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: AiMessage
 * @author: gfs
 * @date: 2025/12/4 11:38
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiMessage {
    //协议
    private Integer agreement;
    //返回给前端的答案
    private String answer;
    //回答的状态 0开始回答，1正在回答，2回答结束
    private Integer status;
}
