package com.javasm.qingqing.adminuser.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogQueryVo {
    private String username;       // 用户名（模糊/精确查询，根据需求调整SQL）
    private String module;         // 操作模块
    private String opContent;      // 操作内容
    private LocalDateTime startTime; // 操作时间-开始（可选）
    private LocalDateTime endTime;   // 操作时间-结束（可选）
}