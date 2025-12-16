package com.javasm.qingqing.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.chat.dao.ChatHistoryDao;
import com.javasm.qingqing.chat.entity.ChatHistory;
import com.javasm.qingqing.chat.service.ChatHistoryService;
import org.springframework.data.redis.connection.zset.Tuple;
import org.springframework.stereotype.Service;

/**
 * (ChatHistory)表服务实现类
 *
 * @author makejava
 * @since 2025-12-04 11:19:34
 */
@Service("chatHistoryService")
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryDao, ChatHistory> implements ChatHistoryService {

    @Override
    public Integer queryIdByMyUidAndToUid(Integer myuid, Integer touid) {
        //已知两个人的uid，查询出这两个人的聊天记录id
        LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistory::getUid,myuid);
        queryWrapper.eq(ChatHistory::getTouid,touid);
        ChatHistory one = getOne(queryWrapper);
        if (one != null){
            return one.getId();
        }
        //如果之前没有聊过
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setUid(myuid);
        chatHistory.setTouid(touid);
        save(chatHistory);
        return chatHistory.getId();
    }
}

