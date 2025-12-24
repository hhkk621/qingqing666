package com.javasm.qingqing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.product.entity.GgProduct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 商品管理-产品查询
 * (GgProduct)表服务接口
 *
 * @author makejava
 * @since 2025-12-23 10:47:21
 */


public interface GgProductService extends IService<GgProduct> {

    /**
     * 导入Excel数据
     *
     * @param file Excel文件
     * @return 导入结果
     */
    Map<String, Object> importExcel(MultipartFile file);

    /**
     * 导出Excel模板
     */
    void exportTemplate(HttpServletResponse response) throws Exception;

    /**
     * 分页查询产品列表
     */
    PageInfo<GgProduct> getProductPage(Integer page, Integer size, String keyword);
}


