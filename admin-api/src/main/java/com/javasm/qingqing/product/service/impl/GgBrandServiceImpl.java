package com.javasm.qingqing.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.product.dao.GgBrandDao;
import com.javasm.qingqing.product.entity.GgBrand;
import com.javasm.qingqing.product.service.GgBrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品管理-品牌管理(GgBrand)表服务实现类
 *
 * @author makejava
 * @since 2025-12-23 20:14:22
 */
@Service("ggBrandService")
public class GgBrandServiceImpl extends ServiceImpl<GgBrandDao, GgBrand> implements GgBrandService {

    @Override
    public PageInfo<GgBrand> getBrandPage(Integer pageNum, Integer pageSize,
                                          String brandName, String brandUrl) {
        // 使用PageHelper进行分页
        PageHelper.startPage(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<GgBrand> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(brandName)) {
            queryWrapper.like(GgBrand::getBrandName, brandName);
        }
        if (StringUtils.isNotBlank(brandUrl)) {
            queryWrapper.like(GgBrand::getBrandUrl, brandUrl);
        }

        // 按ID升序排序
        queryWrapper.orderByAsc(GgBrand::getId);

        // 查询数据
        List<GgBrand> brandList = this.list(queryWrapper);

        // 使用PageInfo包装结果
        return new PageInfo<>(brandList);
    }

    @Override
    public boolean checkBrandNameExists(String brandName) {
        if (StringUtils.isBlank(brandName)) {
            return false;
        }
        LambdaQueryWrapper<GgBrand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GgBrand::getBrandName, brandName);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<GgBrand> searchBrands(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return this.list();
        }

        LambdaQueryWrapper<GgBrand> queryWrapper = new LambdaQueryWrapper<>();

        // 在品牌名称、网址、描述中搜索
        queryWrapper.like(GgBrand::getBrandName, keyword)
                .or()
                .like(GgBrand::getBrandUrl, keyword)
                .or()
                .like(GgBrand::getBrandDescribe, keyword);

        queryWrapper.orderByAsc(GgBrand::getId);

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(GgBrand entity) {
        // 保存前可以添加业务逻辑
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(GgBrand entity) {
        // 更新前可以添加业务逻辑
        return super.updateById(entity);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean removeById(Integer id) {
//        // 删除前可以添加业务逻辑，如检查关联关系
//        return super.removeById(id);
//    }



}

