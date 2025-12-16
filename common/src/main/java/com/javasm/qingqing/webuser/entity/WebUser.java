package com.javasm.qingqing.webuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.common.utils.MD5Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (WebUser)表实体类
 *
 * @author makejava
 * @since 2025-11-29 11:13:15
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebUser extends Model<WebUser> {

    @TableId(type = IdType.AUTO)
    private Integer uid;
    //用户名
    private String username;
    //密码
    private String password;
    //0正常1删除2封号
    private Integer status;
    //邮箱
    private String email;
    //手机号
    private String phone;
    //注册时间
    private Date regTime;

    @TableField(exist = false)
    private WebUserInfo userInfo;

    @TableField(exist = false)
    private WebUserPossession possession;

    public void checkPassword(String pwd){
        if (!MD5Util.encrypt(pwd).equals(password)){
            throw new JavasmException(ExceptionEnum.Password_Error);
        }
    }

}

