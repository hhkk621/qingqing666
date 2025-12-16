package com.javasm.qingqing.skill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.skill.dao.SkillDao;
import com.javasm.qingqing.skill.entity.Skill;
import com.javasm.qingqing.skill.service.SkillService;
import org.springframework.stereotype.Service;

/**
 * 玩家技能(Skill)表服务实现类
 *
 * @author makejava
 * @since 2025-11-29 16:07:10
 */
@Service("skillService")
public class SkillServiceImpl extends ServiceImpl<SkillDao, Skill> implements SkillService {

}

