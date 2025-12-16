package com.javasm.qingqing.adminuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (AdminUser)表实体类
 *
 * @author makejava
 * @since 2025-12-15 11:18:48
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser extends Model<AdminUser> {
    @TableId(type = IdType.AUTO)
    private Long uid;
    //用户名
    private String username;
    //密码
    private String password;
    //角色id
    private Long roleId;
    //注册时间
    private Date regTime;
    //最后登录时间
    private Date loginTime;
    //=1有效 =0无效
    private Integer isvalid;
    //创建人
    private Long createUid;
    //备注
    private String remark;

    private String headImg;
    //昵称
    private String nickname;
    //邮箱
    private String email;
    //手机号
    private String phone;

    @TableField(exist = false)
    private AdminRole role;

}

