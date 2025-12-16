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
 * (AdminRole)表实体类
 *
 * @author makejava
 * @since 2025-12-16 09:54:36
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRole extends Model<AdminRole> {

    @TableId(type = IdType.AUTO)
    private Integer rid;
    //汉字的名称
    private String name;
    //英文标识
    private String code;

    @TableField(exist = false)
    private List<AdminMenu> menuList;

}

