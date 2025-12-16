package com.javasm.qingqing.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (QqTask)表实体类
 *
 * @author makejava
 * @since 2025-12-08 10:38:12
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QqTask extends Model<QqTask> {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //任务名称
    private String name;
    //执行任务的类名
    private String clazz;
    //定时任务的表达式
    private String cron;
    //任务状态 0关闭 1开启
    private Integer status;


}

