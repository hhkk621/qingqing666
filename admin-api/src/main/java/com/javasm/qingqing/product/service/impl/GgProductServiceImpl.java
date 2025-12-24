package com.javasm.qingqing.product.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.product.dao.GgProductDao;
import com.javasm.qingqing.product.entity.GgProduct;
import com.javasm.qingqing.product.listener.GgProductExcelListener;
import com.javasm.qingqing.product.service.GgProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理-产品查询
 * (GgProduct)表服务实现类
 *
 * @author makejava
 * @since 2025-12-23 10:47:22
 */
@Service("ggProductService")
@Slf4j

public class GgProductServiceImpl extends ServiceImpl<GgProductDao, GgProduct> implements GgProductService {

    @Override
    public Map<String, Object> importExcel(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "请选择要上传的文件");
            return result;
        }

        // 检查文件类型
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            result.put("success", false);
            result.put("message", "只支持.xlsx和.xls格式的Excel文件");
            return result;
        }

        try {
            // 创建监听器
            GgProductExcelListener listener = new GgProductExcelListener(this);

            // 使用EasyExcel读取Excel文件
            EasyExcel.read(file.getInputStream(), GgProduct.class, listener)
                    .sheet()  // 默认读取第一个sheet
                    .headRowNumber(1)  // 表头行数，从1开始计数
                    .doRead();

            // 获取导入结果
            int total = listener.getDataList().size();
            int success = listener.getSuccessCount();
            int fail = listener.getFailCount();

            result.put("success", true);
            result.put("message", String.format("导入完成！共处理%d条数据，成功%d条，失败%d条", total, success, fail));
            result.put("total", total);
            result.put("successCount", success);
            result.put("failCount", fail);
            result.put("errorMessages", listener.getErrorMessages());

        } catch (IOException e) {
            log.error("读取Excel文件失败: ", e);
            result.put("success", false);
            result.put("message", "读取文件失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("导入Excel数据异常: ", e);
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public void exportTemplate(HttpServletResponse response) throws Exception {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 防止中文乱码
        String fileName = URLEncoder.encode("产品信息导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 创建一个空的Excel文件，只包含表头
        EasyExcel.write(response.getOutputStream(), GgProduct.class)
                .sheet("产品信息")
                .doWrite(Collections.emptyList());
    }

    @Override
    public PageInfo<GgProduct> getProductPage(Integer page, Integer size, String keyword) {
        // 设置分页参数
        PageHelper.startPage(page, size);

        // 构建查询条件
        LambdaQueryWrapper<GgProduct> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like(GgProduct::getCoding, keyword)
                    .or()
                    .like(GgProduct::getBrand, keyword)
                    .or()
                    .like(GgProduct::getModel, keyword)
                    .or()
                    .like(GgProduct::getClassify, keyword);
        }

        queryWrapper.orderByAsc(GgProduct::getId);

        // 查询数据
        List<GgProduct> productList = this.list(queryWrapper);

        // 使用PageInfo包装结果
        return new PageInfo<>(productList);
    }
}

