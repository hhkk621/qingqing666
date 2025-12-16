package com.javasm.qingqing.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.chat.entity.ChatHistoryInfo;

import java.util.List;

/**
 * (ChatHistoryInfo)表服务接口
 *
 * @author makejava
 * @since 2025-12-04 11:19:44
 */
public interface ChatHistoryInfoService extends IService<ChatHistoryInfo> {
    //保存聊天记录
    void saveHistory(Integer chatId,Integer uid,String msg);

    //根据聊天id 查询聊天记录
    List<ChatHistoryInfo> queryListByChatId(Integer chatId);

    void saveHistory(Integer chatId, Integer touid, List<String> saveList);
}

