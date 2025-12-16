package com.javasm.qingqing.chat.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (ChatHistoryInfo)表实体类
 *
 * @author makejava
 * @since 2025-12-04 11:19:44
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryInfo extends Model<ChatHistoryInfo> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer chatId;

    private Integer uid;

    private String message;

    private Date ctime;


}

