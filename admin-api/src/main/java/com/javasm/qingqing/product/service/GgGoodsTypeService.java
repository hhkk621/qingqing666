package com.javasm.qingqing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.product.entity.GgGoodsType;

/**
 * 商品管理-商品类型表(GgGoodsType)表服务接口
 *
 * @author makejava
 * @since 2025-12-24 10:12:02
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.product.entity.GgGoodsType;
import java.util.List;

/**
 * 商品类型表服务接口
 */
public interface GgGoodsTypeService extends IService<GgGoodsType> {

    /**
     * 获取商品类型树
     */
    List<GgGoodsType> getGoodsTypeTree();

    /**
     * 获取子类型列表
     */
    List<GgGoodsType> getChildrenByPid(String pid);

    /**
     * 新增商品类型
     */
    boolean addGoodsType(GgGoodsType goodsType);

    /**
     * 修改商品类型
     */
    boolean updateGoodsType(GgGoodsType goodsType);

    /**
     * 删除商品类型
     */
    boolean deleteGoodsType(String id);

    /**
     * 检查是否存在子类型
     */
    boolean hasChildren(String id);

    /**
     * 搜索商品类型
     */
    List<GgGoodsType> searchGoodsTypes(String keyword, String pid);

    /**
     * 获取所有父级类型选项
     */
    List<GgGoodsType> getAllParentOptions();
}

