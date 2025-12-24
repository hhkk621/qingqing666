package com.javasm.qingqing.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javasm.qingqing.annotation.OperateContent;
import com.javasm.qingqing.annotation.OperateModule;
import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.product.entity.GgGoodsType;
import com.javasm.qingqing.product.service.GgGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品类型管理控制器
 */
@OperateModule(value = "商品类型管理")
@RestController
@RequestMapping("/gg/goodsType")
public class GgGoodsTypeController {
    
    @Autowired
    private GgGoodsTypeService goodsTypeService;
    
    /**
     * 获取商品类型树
     */
    @OperateContent(value = "获取商品类型树")
    @GetMapping("/tree")
    public R getTree() {
        List<GgGoodsType> tree = goodsTypeService.getGoodsTypeTree();
        return R.ok(tree);
    }
    
    /**
     * 分页查询商品类型列表
     */
    @OperateContent(value = "分页查询商品类型列表")
    @GetMapping("/page")
    public R page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) String pid) {
        
        Page<GgGoodsType> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<GgGoodsType> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(typeName)) {
            queryWrapper.like(GgGoodsType::getTypeName, typeName);
        }
        
        if (StringUtils.hasText(pid)) {
            queryWrapper.eq(GgGoodsType::getPid, pid);
        } else {
            // 默认只查询根节点
            queryWrapper.eq(GgGoodsType::getPid, "-1");
        }
        
        queryWrapper.orderByAsc(GgGoodsType::getCreateTime);
        
        IPage<GgGoodsType> result = goodsTypeService.page(page, queryWrapper);
        return R.ok(result);
    }
    
    /**
     * 根据ID查询商品类型
     */
    @OperateContent(value = "根据ID查询商品类型")
    @GetMapping("/{id}")
    public R getById(@PathVariable String id) {
        GgGoodsType goodsType = goodsTypeService.getById(id);
        if (goodsType == null) {
            return R.error(404, "商品类型不存在");
        }
        return R.ok(goodsType);
    }
    
    /**
     * 查询子类型列表
     */
    @OperateContent(value = "查询子类型列表")
    @GetMapping("/children/{pid}")
    public R getChildren(@PathVariable String pid) {
        List<GgGoodsType> children = goodsTypeService.getChildrenByPid(pid);
        return R.ok(children);
    }
    
    /**
     * 新增商品类型
     */
    @OperateContent(value = "新增商品类型")
    @PostMapping
    public R add(@RequestBody GgGoodsType goodsType) {
        boolean success = goodsTypeService.addGoodsType(goodsType);
        return R.ok(success);
    }
    
    /**
     * 修改商品类型
     */
    @OperateContent(value = "修改商品类型")
    @PutMapping
    public R update(@RequestBody GgGoodsType goodsType) {
        boolean success = goodsTypeService.updateGoodsType(goodsType);
        return R.ok(success);
    }
    
    /**
     * 删除商品类型
     */
    @OperateContent(value = "删除商品类型")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable String id) {
        boolean success = goodsTypeService.deleteGoodsType(id);
        return R.ok(success);
    }
    
    /**
     * 批量删除商品类型
     */
    @OperateContent(value = "批量删除商品类型")
    @DeleteMapping("/batch")
    public R batchDelete(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.error("请选择要删除的商品类型");
        }
        
        boolean allSuccess = true;
        for (String id : ids) {
            if (!goodsTypeService.deleteGoodsType(id)) {
                allSuccess = false;
            }
        }
        
        return R.ok(allSuccess);
    }
    
    /**
     * 查询所有父级类型选项（用于下拉选择）
     */
    @OperateContent(value = "查询所有父级类型选项")
    @GetMapping("/parentOptions")
    public R getParentOptions() {
        List<GgGoodsType> allTypes = goodsTypeService.getAllParentOptions();
        return R.ok(allTypes);
    }
    
    /**
     * 搜索商品类型
     */
    @OperateContent(value = "搜索商品类型")
    @GetMapping("/search")
    public R search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String pid) {
        List<GgGoodsType> result = goodsTypeService.searchGoodsTypes(keyword, pid);
        return R.ok(result);
    }
}