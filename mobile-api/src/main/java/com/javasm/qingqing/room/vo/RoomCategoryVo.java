package com.javasm.qingqing.room.vo;

import lombok.Data;

/**
 * @className: RoomCategoryVo
 * @author: gfs
 * @date: 2025/12/4
 * @version: 0.1
 * @since: jdk17
 * @description: 房间分类信息VO
 */
@Data
public class RoomCategoryVo {
    //大厅类型编号
    private Integer categoryId;
    //大厅类型名称
    private String categoryName;
    //大厅类型logo 备用
    private String categoryLogo;
    //上级分类编号
    private Integer parentId;
}