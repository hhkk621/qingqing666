package com.javasm.qingqing.product.controller;

import com.javasm.qingqing.annotation.OperateContent;
import com.javasm.qingqing.annotation.OperateModule;
import com.javasm.qingqing.product.entity.GgBrand;
import com.javasm.qingqing.product.service.GgBrandService;
import com.javasm.qingqing.common.exception.R;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gg/brand")
@OperateModule(value = "品牌管理")
public class GgBrandController {

    private final GgBrandService ggBrandService;

    @GetMapping("/list")
    @OperateContent(value = "分页查询品牌列表")
    public R getBrandList(

            @RequestParam(defaultValue = "1") Integer pageNum,


            @RequestParam(defaultValue = "10") Integer pageSize,


            @RequestParam(required = false) String brandName,


            @RequestParam(required = false) String brandUrl) {

        try {
            PageInfo<GgBrand> pageInfo = ggBrandService.getBrandPage(
                    pageNum, pageSize, brandName, brandUrl);
            return R.ok(pageInfo);
        } catch (Exception e) {
            log.error("查询品牌列表失败: ", e);
            return R.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @OperateContent
            (value = "根据ID获取品牌详情")
    public R getBrandById(
            @PathVariable Integer id) {

        try {
            GgBrand brand = ggBrandService.getById(id);
            if (brand == null) {
                return R.error(404, "品牌不存在");
            }
            return R.ok(brand);
        } catch (Exception e) {
            log.error("获取品牌详情失败: ", e);
            return R.error("获取详情失败：" + e.getMessage());
        }
    }

    @PostMapping
    @OperateContent
            (value = "新增品牌")
    public R addBrand(
            @Valid @RequestBody GgBrand brand) {

        try {
            // 检查品牌名称是否已存在
            if (ggBrandService.checkBrandNameExists(brand.getBrandName())) {
                return R.error("品牌名称已存在");
            }

            boolean result = ggBrandService.save(brand);
            if (result) {
                return R.ok("新增成功");
            } else {
                return R.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增品牌失败: ", e);
            return R.error("新增失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @OperateContent
            (value = "修改品牌")
    public R updateBrand(

            @PathVariable Integer id,

            @Valid @RequestBody GgBrand brand) {

        try {
            // 检查品牌是否存在
            GgBrand existingBrand = ggBrandService.getById(id);
            if (existingBrand == null) {
                return R.error(404, "品牌不存在");
            }

            // 如果修改了名称，检查新名称是否与其他品牌冲突
            if (!existingBrand.getBrandName().equals(brand.getBrandName())) {
                if (ggBrandService.checkBrandNameExists(brand.getBrandName())) {
                    return R.error("品牌名称已存在");
                }
            }

            brand.setId(id);
            boolean result = ggBrandService.updateById(brand);
            if (result) {
                return R.ok(brand);
            } else {
                return R.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改品牌失败: ", e);
            return R.error("修改失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @OperateContent
            (value = "删除品牌")
    public R deleteBrand(

            @PathVariable Integer id) {

        try {
            // 检查品牌是否存在
            GgBrand existingBrand = ggBrandService.getById(id);
            if (existingBrand == null) {
                return R.error(404, "品牌不存在");
            }

            boolean result = ggBrandService.removeById(id);
            if (result) {
                return R.ok("删除成功");
            } else {
                return R.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除品牌失败: ", e);
            return R.error("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch/delete")
    @OperateContent
            (value = "批量删除品牌")
    public R batchDeleteBrand(

            @RequestBody List<Integer> ids) {

        try {
            if (ids == null || ids.isEmpty()) {
                return R.error("请选择要删除的品牌");
            }

            boolean result = ggBrandService.removeByIds(ids);
            if (result) {
                return R.ok("批量删除成功");
            } else {
                return R.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除品牌失败: ", e);
            return R.error("批量删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/check/name")
    @OperateContent
            (value = "检查品牌名称")
    public R checkBrandNameExists(
            @RequestParam String brandName) {

        try {
            boolean exists = ggBrandService.checkBrandNameExists(brandName);
            return R.ok((exists ? "品牌名称已存在" : "品牌名称可用"));
        } catch (Exception e) {
            log.error("检查品牌名称失败: ", e);
            return R.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/all")
    @OperateContent
            (value = "获取所有品牌")
    public R getAllBrands() {
        try {
            List<GgBrand> brandList = ggBrandService.list();
            return R.ok(brandList);
        } catch (Exception e) {
            log.error("获取所有品牌失败: ", e);
            return R.error("获取失败：" + e.getMessage());
        }
    }

    @GetMapping("/search")
    @OperateContent
            (value = "搜索品牌")
    public R searchBrands(
            @RequestParam String keyword) {

        try {
            List<GgBrand> brandList = ggBrandService.searchBrands(keyword);
            return R.ok(brandList);
        } catch (Exception e) {
            log.error("搜索品牌失败: ", e);
            return R.error("搜索失败：" + e.getMessage());
        }
    }
}