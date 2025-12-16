package com.javasm.qingqing.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (ChatHistory)表实体类
 *
 * @author makejava
 * @since 2025-12-04 11:19:34
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory extends Model<ChatHistory> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer touid;

    private String title;
    @TableField(exist = false)
    private List<ChatHistoryInfo> chatInfoList;


}

