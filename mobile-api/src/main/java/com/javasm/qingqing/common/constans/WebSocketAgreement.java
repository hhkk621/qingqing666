package com.javasm.qingqing.common.constans;

/**
 * @className: WebSocketAgreement
 * @author: gfs
 * @date: 2025/12/4 14:37
 * @version: 0.1
 * @since: jdk17
 * @description: WebSocket协议常量定义接口
 */
public interface WebSocketAgreement {

    /**
     * 聊天历史记录消息类型
     */
    Integer Chat_History = 2001;

    /**
     * AI提问消息类型
     */
    Integer AI_Question = 3001;
    Integer AI_Question_Image = 3004;

    /**
     * AI回答消息类型
     */
    Integer AI_Answer = 3002;
    /**
     * 图片答案
     */
    Integer AI_Answer_Image = 3003;
}