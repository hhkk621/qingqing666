package com.javasm.qingqing.webuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (WebUserLoginLog)表实体类
 *
 * @author makejava
 * @since 2025-11-29 11:56:01
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebUserLoginLog extends Model<WebUserLoginLog> {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户id
    private Integer uid;
    //登录时间
    private Date loginTime;


}

