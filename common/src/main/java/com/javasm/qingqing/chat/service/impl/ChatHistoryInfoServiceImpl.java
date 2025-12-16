package com.javasm.qingqing.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.chat.dao.ChatHistoryInfoDao;
import com.javasm.qingqing.chat.entity.ChatHistoryInfo;
import com.javasm.qingqing.chat.service.ChatHistoryInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * (ChatHistoryInfo)表服务实现类
 *
 * @author makejava
 * @since 2025-12-04 11:19:44
 */
@Service("chatHistoryInfoService")
public class ChatHistoryInfoServiceImpl extends ServiceImpl<ChatHistoryInfoDao, ChatHistoryInfo> implements ChatHistoryInfoService {

    @Override
    public void saveHistory(Integer chatId, Integer uid, String msg) {
        ChatHistoryInfo chatHistoryInfo = new ChatHistoryInfo();
        chatHistoryInfo.setChatId(chatId);
        chatHistoryInfo.setUid(uid);
        chatHistoryInfo.setMessage(msg);
        chatHistoryInfo.setCtime(new Date());
        save(chatHistoryInfo);
    }
    @Override
    @Transactional
    public void saveHistory(Integer chatId, Integer touid, List<String> urlList) {
        List<ChatHistoryInfo> saveList = new ArrayList<>();
        urlList.forEach(url ->{
            ChatHistoryInfo chatHistoryInfo = new ChatHistoryInfo();
            chatHistoryInfo.setChatId(chatId);
            chatHistoryInfo.setUid(touid);
            chatHistoryInfo.setMessage(url);
            chatHistoryInfo.setCtime(new Date());
            saveList.add(chatHistoryInfo);
        });
        saveBatch(saveList);
    }
    @Override
    public List<ChatHistoryInfo> queryListByChatId(Integer chatId) {
        //根据聊天id 查询聊天记录
        LambdaQueryWrapper<ChatHistoryInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistoryInfo::getChatId,chatId);
        //查询最后5条聊天记录
        queryWrapper.orderByDesc(ChatHistoryInfo::getCtime);
        queryWrapper.last("limit 5");
        //聊天记录的结果
        List<ChatHistoryInfo> list = list(queryWrapper);
        //按照id 正序排列
        list.sort((Comparator.comparing(ChatHistoryInfo::getId)));
        return list;
    }


}

