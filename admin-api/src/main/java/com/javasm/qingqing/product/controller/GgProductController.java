package com.javasm.qingqing.product.controller;

import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.annotation.OperateContent;
import com.javasm.qingqing.annotation.OperateModule;
import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.product.entity.GgProduct;
import com.javasm.qingqing.product.service.GgProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gg/product")
@OperateModule(value = "产品管理接口")
public class GgProductController {

    private final GgProductService ggProductService;

    @PostMapping("/import")
    @OperateContent(value = "导入Excel数据")
    public R importExcel(@RequestParam("file") MultipartFile file) {
        log.info("开始导入Excel数据，文件名: {}, 文件大小: {} bytes",
                file.getOriginalFilename(), file.getSize());

        try {
            Map<String, Object> result = ggProductService.importExcel(file);

            if (Boolean.TRUE.equals(result.get("success"))) {
                return R.ok(result);
            } else {
                return R.error((String) result.getOrDefault("message", "导入失败"));
            }

        } catch (Exception e) {
            log.error("导入Excel异常: ", e);
            return R.error("导入失败：" + e.getMessage());
        }
    }

    @GetMapping("/template/download")
    @OperateContent(value = "下载导入模板")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 文件名编码处理
            String fileName = URLEncoder.encode("产品导入模板.xls", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

            // 从 resources/templates 目录读取模板文件
            ClassPathResource resource = new ClassPathResource("templates/product_import_template.xls");
            InputStream inputStream = resource.getInputStream();

            // 将文件内容写入响应
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            inputStream.close();

        } catch (Exception e) {
            log.error("下载模板失败: ", e);
            try {
                response.sendError(500, "下载模板失败: " + e.getMessage());
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @GetMapping("/list")
    @OperateContent(value = "获取产品列表")
    public R getProductList(
            @RequestParam(defaultValue = "1",value = "currentPage") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {

        try {
            PageInfo<GgProduct> productPage = ggProductService.getProductPage(pageNum, pageSize, keyword);
            return R.ok(productPage);
        } catch (Exception e) {
            log.error("查询产品列表失败: ", e);
            return R.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @OperateContent(value = "根据ID获取产品详情")
    public R getProductById(@PathVariable Integer id) {
        try {
            GgProduct product = ggProductService.getById(id);
            if (product == null) {
                return R.error("产品不存在");
            }
            return R.ok(product);
        } catch (Exception e) {
            log.error("获取产品详情失败: ", e);
            return R.error("获取产品详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    @OperateContent(value = "新增产品")
    public R addProduct(@RequestBody GgProduct product) {
        try {
            // 校验必要字段
            if (product.getCoding() == null || product.getCoding().trim().isEmpty()) {
                return R.error("产品编码不能为空");
            }

            boolean result = ggProductService.save(product);
            if (result) {
                return R.ok("新增成功");
            } else {
                return R.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增产品失败: ", e);
            return R.error("新增失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @OperateContent(value = "修改产品")
    public R updateProduct(@PathVariable Integer id, @RequestBody GgProduct product) {
        try {
            // 检查产品是否存在
            GgProduct existingProduct = ggProductService.getById(id);
            if (existingProduct == null) {
                return R.error("产品不存在");
            }

            product.setId(id);
            boolean result = ggProductService.updateById(product);
            if (result) {
                return R.ok("修改成功");
            } else {
                return R.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改产品失败: ", e);
            return R.error("修改失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @OperateContent(value = "删除产品")
    public R deleteProduct(@PathVariable Integer id) {
        try {
            boolean result = ggProductService.removeById(id);
            if (result) {
                return R.ok("删除成功");
            } else {
                return R.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除产品失败: ", e);
            return R.error("删除失败: " + e.getMessage());
        }
    }
}