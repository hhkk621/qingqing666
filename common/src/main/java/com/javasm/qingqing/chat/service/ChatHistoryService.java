package com.javasm.qingqing.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.chat.entity.ChatHistory;

/**
 * (ChatHistory)表服务接口
 *
 * @author makejava
 * @since 2025-12-04 11:19:34
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    //传入两聊天用户的UID，返回聊天的id
    Integer queryIdByMyUidAndToUid(Integer myuid,Integer touid);
}

