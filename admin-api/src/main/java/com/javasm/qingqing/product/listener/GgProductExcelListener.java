package com.javasm.qingqing.product.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.javasm.qingqing.product.entity.GgProduct;
import com.javasm.qingqing.product.service.GgProductService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Getter
public class GgProductExcelListener implements ReadListener<GgProduct> {
    
    private static final int BATCH_COUNT = 100;
    
    // 存储所有解析的数据
    private final List<GgProduct> dataList = ListUtils.newArrayList();
    
    // 存储批量提交的数据
    private List<GgProduct> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    
    private final GgProductService ggProductService;
    
    // 统计信息
    private int successCount = 0;
    private int failCount = 0;
    private final StringBuilder errorMessages = new StringBuilder();
    
    public GgProductExcelListener(GgProductService ggProductService) {
        this.ggProductService = ggProductService;
    }
    
    /**
     * 每读取一行数据调用
     */
    @Override
    public void invoke(GgProduct data, AnalysisContext context) {
        try {
            // 1. 数据校验
            if (!validateData(data)) {
                failCount++;
                return;
            }
            
            // 2. 数据处理
            processData(data);
            
            // 3. 添加到列表
            dataList.add(data);
            cachedDataList.add(data);
            
            // 4. 达到批量大小，保存到数据库
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
            
        } catch (Exception e) {
            failCount++;
            String errorMsg = String.format("第%d行数据处理失败: %s", context.readRowHolder().getRowIndex() + 1, e.getMessage());
            log.error(errorMsg);
            errorMessages.append(errorMsg).append("\n");
        }
    }
    
    /**
     * 所有数据解析完成调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 保存最后一批数据
        if (!cachedDataList.isEmpty()) {
            saveData();
        }
        log.info("Excel数据解析完成！成功: {}条, 失败: {}条", successCount, failCount);
    }
    
    /**
     * 批量保存数据
     */
    private void saveData() {
        if (cachedDataList.isEmpty()) {
            return;
        }
        
        try {
            boolean result = ggProductService.saveBatch(cachedDataList);
            if (result) {
                successCount += cachedDataList.size();
                log.info("批量保存成功，本次保存{}条", cachedDataList.size());
            } else {
                failCount += cachedDataList.size();
                log.error("批量保存失败，本次{}条", cachedDataList.size());
                errorMessages.append("批量保存失败，影响").append(cachedDataList.size()).append("条数据\n");
            }
        } catch (Exception e) {
            failCount += cachedDataList.size();
            log.error("批量保存异常: ", e);
            errorMessages.append("批量保存异常: ").append(e.getMessage()).append("\n");
            
            // 尝试逐条保存
            for (GgProduct product : cachedDataList) {
                try {
                    ggProductService.save(product);
                    successCount++;
                } catch (Exception ex) {
                    failCount++;
                    log.error("保存单条数据失败: {}", product, ex);
                }
            }
        }
    }
    
    /**
     * 数据校验
     */
    private boolean validateData(GgProduct data) {
        // 1. 必填字段校验
        if (!StringUtils.hasText(data.getCoding())) {
            log.error("coding不能为空");
            return false;
        }
        
        if (!StringUtils.hasText(data.getClassify())) {
            log.error("classify不能为空");
            return false;
        }
        
        if (!StringUtils.hasText(data.getBrand())) {
            log.error("brand不能为空");
            return false;
        }
        
        if (!StringUtils.hasText(data.getModel())) {
            log.error("model不能为空");
            return false;
        }
        
        // 2. 格式校验
        if (data.getCoding().length() > 64) {
            log.error("coding长度不能超过64位");
            return false;
        }
        
        return true;
    }
    
    /**
     * 数据处理
     */
    private void processData(GgProduct data) {
        // 设置默认值
        if (!StringUtils.hasText(data.getBusiness())) {
            data.setBusiness("无");
        }
        
        if (!StringUtils.hasText(data.getPurchasingPattern())) {
            data.setPurchasingPattern("集团采购");
        }
        
        if (!StringUtils.hasText(data.getDistribution())) {
            data.setDistribution("非铺货");
        }
        
        if (!StringUtils.hasText(data.getStandby())) {
            data.setStandby("非备机");
        }
        
        // 处理颜色字段（Excel中是数字）
        if (data.getColor() == null) {
            data.setColor(0); // 默认白色
        }
        
        // 设置时间
        Date now = new Date();
        data.setCreateTime(now);
        data.setUpdateTime(now);
    }
    
    /**
     * 获取导入统计信息
     */
    public String getImportStats() {
        return String.format("成功导入%d条，失败%d条", successCount, failCount);
    }
    
    /**
     * 获取错误信息
     */
    public String getErrorMessages() {
        return errorMessages.toString();
    }
}