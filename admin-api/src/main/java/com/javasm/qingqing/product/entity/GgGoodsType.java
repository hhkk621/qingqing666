package com.javasm.qingqing.product.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品管理-商品类型表(GgGoodsType)表实体类
 *
 * @author makejava
 * @since 2025-12-24 10:12:01
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "gg_goods_type")
public class GgGoodsType extends Model<GgGoodsType> {

    @TableId(type = IdType.AUTO)
    private String id;

    private String typeName;
    @TableField(value = "`desc`")
    private String desc;

    private Date createTime;

    private Date updateTime;

    private String pid;

    @TableField(exist = false)
    private List<GgGoodsType> children;

}

