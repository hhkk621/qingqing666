package com.javasm.qingqing.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陪玩直播大厅(Room)表实体类
 *
 * @author makejava
 * @since 2025-12-02 09:41:26
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room extends Model<Room> {
    //陪玩大厅
    @TableId(type = IdType.AUTO)
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

