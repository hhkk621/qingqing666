package com.javasm.qingqing.webuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (WebUserAi)表实体类
 *
 * @author makejava
 * @since 2025-12-04 11:20:52
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebUserAi extends Model<WebUserAi> {

    @TableId(type = IdType.INPUT)
    private Integer uid;
    //人物性格
    @TableField("`character`")
    private String character;
    //模型
    private String model;


}

