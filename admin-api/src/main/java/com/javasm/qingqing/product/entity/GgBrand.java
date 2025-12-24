package com.javasm.qingqing.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品管理-品牌管理(GgBrand)表实体类
 *
 * @author makejava
 * @since 2025-12-23 20:14:22
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GgBrand extends Model<GgBrand> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //品牌名称
    private String brandName;
    //品牌网址
    private String brandUrl;
    //品牌描述
    private String brandDescribe;


}

