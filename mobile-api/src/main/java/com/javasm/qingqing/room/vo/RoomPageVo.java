package com.javasm.qingqing.room.vo;

import lombok.Data;

import java.util.List;

/**
 * @className: RoomPageVo
 * @author: gfs
 * @date: 2025/12/4
 * @version: 0.1
 * @since: jdk17
 * @description: 房间分页查询结果VO
 */
@Data
public class RoomPageVo {
    // 当前页码
    private Integer curpage;
    // 总记录数
    private Integer allnum;
    // 房间列表
    private List<RoomVo> roomlist;
}