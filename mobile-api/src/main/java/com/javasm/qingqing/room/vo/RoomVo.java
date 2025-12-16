package com.javasm.qingqing.room.vo;

import lombok.Data;

/**
 * @className: RoomVo
 * @author: gfs
 * @date: 2025/12/4
 * @version: 0.1
 * @since: jdk17
 * @description: 房间信息VO
 */
@Data
public class RoomVo {
    //陪玩大厅ID
    private Integer id;
    //大厅名称
    private String name;
    //大厅所属人
    private Integer ownerCustomer;
    //封面
    private String cover;
    //大厅大类
    private Integer categoryL1;
    //大厅小类
    private Integer categoryL2;
    //大厅禁言状态 0禁止 1正常
    private Integer speechStatus;
    //大厅公告
    private String notice;
    //大厅推荐 1正常 2首页推荐
    private Integer push;
    //大厅开启状态 0禁止 1正常
    private Integer openStatus;
}