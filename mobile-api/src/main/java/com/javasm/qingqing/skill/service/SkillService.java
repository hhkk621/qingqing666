package com.javasm.qingqing.skill.service;

import com.javasm.qingqing.skill.entity.Skill;
import com.javasm.qingqing.skill.vo.SkillSaveVo;

import java.util.List;

/**
 * @className: SkillService
 * @author: 
 * @date: 2025/12/4
 * @version: 0.1
 * @since: jdk17
 * @description: Skill业务层接口
 */
public interface SkillService {
    
    /**
     * 查询所有技能列表
     * @return 技能列表
     */
    List<Skill> getAllSkills();

    /**
     * 根据ID查询技能
     * @param id 技能ID
     * @return 技能信息
     */
    Skill getSkillById(Integer id);

    /**
     * 添加技能
     * @param vo 技能信息
     * @return 是否添加成功
     */
    boolean addSkill(SkillSaveVo vo);

    /**
     * 更新技能信息
     * @param vo 技能信息
     * @return 是否更新成功
     */
    boolean updateSkill(SkillSaveVo vo);

    /**
     * 删除技能
     * @param id 技能ID
     * @return 是否删除成功
     */
    boolean deleteSkill(Integer id);
}