package com.javasm.qingqing.adminuser.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统日志表(SysLog)表实体类
 *
 * @author makejava
 * @since 2025-12-20 15:13:48
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLog extends Model<SysLog> {


    //日志唯一标识（自增主键）
    @TableId(type = IdType.AUTO)
    private Integer id;
    //操作时间（精确到秒）
    private Date opTime;
    //访问用户名（如“张三”“赵四”）
    private String username;
    //访问模块（如“财务管理”）
    private String module;
    //操作内容（如“登录系统”“修改权限”）
    private String opContent;



}

