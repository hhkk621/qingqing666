package com.javasm.qingqing.skill.vo;

import lombok.Data;

/**
 * @className: SkillSaveVo
 * @author: gfs
 * @date: 2025/12/3 16:03
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class SkillSaveVo {
    //这里的属性名，和被调用一方的参数json字符串匹配
    private String gameUsername;
    private Double skillPrice;
    private Integer skillId;
}
