package com.javasm.qingqing.webuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 陪玩用户表(WebUserInfo)表实体类
 *
 * @author makejava
 * @since 2025-11-29 11:40:38
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebUserInfo extends Model<WebUserInfo> {
    //用户编号
    @TableId(type = IdType.INPUT)
    private Integer uid;
    //用户名
    private String nickname;
    //用户vip等级
    private Integer vipGrade;
    //头像
    private String headPic;
    //贵族级别
    private Integer loadGrade;
    //是否大神用户 0普通用户 1大神用户
    private Integer godStatus;
    //粉丝数量
    private Integer fansNum;
    //IP归属地
    private String ipAddress;
    //个人说明
    private String remark;
    //喜欢的游戏说明
    private String hobbyGame;
    //喜欢的音乐说明
    private String hobbyMusic;
    //封停状态 0封停 1正常
    private String lockoutStatus;
    //解封时间
    private Date lockoutTime;
    //生日
    private String birthday;
    //城市
    private String city;
    //性别 0保密1男2女
    private Integer gender;
    //心情
    private String mood;
    //简介
    private String intro;
    //0自然人1ai
    private Integer isai;


}

