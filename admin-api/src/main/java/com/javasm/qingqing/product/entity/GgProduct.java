package com.javasm.qingqing.product.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品管理-产品查询
 * (GgProduct)表实体类
 *
 * @author makejava
 * @since 2025-12-23 10:47:21
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("gg_product")
public class GgProduct {
    @ExcelIgnore  // Excel导入时忽略此字段（自增主键由数据库生成）
    @TableId(type = IdType.AUTO)
    private Integer id;

    // Excel列映射
    @ExcelProperty("coding")
    private String coding;

    @ExcelProperty("classify")
    private String classify;

    @ExcelProperty("brand")
    private String brand;

    @ExcelProperty("model")
    private String model;

    @ExcelProperty("business")
    private String business = "无";  // 默认值

    // Excel中可能是字符串数字，需要转换
    @ExcelProperty("color")
    private Integer color;

    @ExcelProperty("purchasingPattern")
    private String purchasingPattern = "集团采购";

    @ExcelProperty("distribution")
    private String distribution = "非铺货";

    @ExcelProperty("standby")
    private String standby = "非备机";

    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private Date updateTime;
}

