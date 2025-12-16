package com.javasm.qingqing.skill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 玩家技能(Skill)表实体类
 *
 * @author makejava
 * @since 2025-11-29 16:07:09
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends Model<Skill> {
    //技能资质编号
    @TableId(type = IdType.AUTO)
    private Integer skillId;
    //用户编号
    private Integer uid;
    //游戏名称编号
    private Integer gameId;
    //所在区服编号
    private Integer gameServerId;
    //游戏段位编号
    private Integer gameLevel;
    //游戏昵称
    private String gameUsername;
    //陪玩单价
    private Double skillPrice;
    //技能资质图片
    private String skillImgs;
    //促销方案0无促销 1.限时8折优惠 2.买二送一
    private Integer skillPromotion;
    //接单次数
    private Integer orderTimes;


}

