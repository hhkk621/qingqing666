package com.javasm.qingqing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.product.entity.GgBrand;

import java.util.List;

/**
 * 商品管理-品牌管理(GgBrand)表服务接口
 *
 * @author makejava
 * @since 2025-12-23 20:14:22
 */
public interface GgBrandService extends IService<GgBrand> {
    /**
     * 分页查询品牌列表
     */
    PageInfo<GgBrand> getBrandPage(Integer pageNum, Integer pageSize,
                                   String brandName, String brandUrl);

    /**
     * 检查品牌名称是否存在
     */
    boolean checkBrandNameExists(String brandName);

    /**
     * 根据关键词搜索品牌
     */
    List<GgBrand> searchBrands(String keyword);
}

