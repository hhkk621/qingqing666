package com.javasm.qingqing.adminuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (RelAdminRoleMenu)表实体类
 *
 * @author makejava
 * @since 2025-12-17 16:06:22
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelAdminRoleMenu extends Model<RelAdminRoleMenu> {

    @TableId(type = IdType.AUTO)
    //角色id
    private Integer rid;
    //菜单id
    private Integer mid;


}


