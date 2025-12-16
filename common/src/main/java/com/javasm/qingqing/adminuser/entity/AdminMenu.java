package com.javasm.qingqing.adminuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * (AdminMenu)表实体类
 *
 * @author makejava
 * @since 2025-12-16 09:54:46
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenu extends Model<AdminMenu> {
    //主键
    @TableId(type = IdType.AUTO)
    private Integer mid;
    //菜单名称
    private String name;
    //访问地址
    private String url;
    //父菜单id
    private Integer pid;
    //图标
    private String icon;
    //状态1正常0关闭
    private Integer status;
    //排序
    private Integer sort;

    @TableField(exist = false)
    private List<AdminMenu> childList;

}

