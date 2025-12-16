package com.javasm.qingqing.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.javasm.qingqing.game.vo.HomeGameVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 技能所属游戏(Game)表实体类
 *
 * @author makejava
 * @since 2025-11-29 15:59:45
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game extends Model<Game> {
    //技能游戏编号
    @TableId(type = IdType.AUTO)
    private Integer id;
    //技能游戏名称
    private String name;
    //游戏图标
    private String icon;
    //是否热门0不热门1热门
    private Integer ishot;
    //排序字段
    private Integer sort;

    //当前游戏的首页推荐信息
    @TableField(exist = false)
    private List<HomeGameVo> gameSkillList;

}

