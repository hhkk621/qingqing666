package com.javasm.qingqing.chat.vo;

import com.javasm.qingqing.chat.entity.ChatHistory;
import com.javasm.qingqing.chat.entity.ChatHistoryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @className: ChatHistoryMessage
 * @author: gfs
 * @date: 2025/12/4 14:33
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryMessage {
    //协议号
    private Integer agreement;

    private List<ChatHistoryInfo> list;
}
