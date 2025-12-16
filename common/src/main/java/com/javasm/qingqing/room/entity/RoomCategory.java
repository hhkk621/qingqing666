package com.javasm.qingqing.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陪玩直播大厅分类(RoomCategory)表实体类
 *
 * @author makejava
 * @since 2025-12-02 09:46:18
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCategory extends Model<RoomCategory> {
    //大厅类型编号
    @TableId(type = IdType.AUTO)
    private Integer categoryId;
    //大厅类型名称
    private String categoryName;
    //大厅类型logo 备用
    private String categoryLogo;
    //上级分类编号
    private Integer parentId;


}

