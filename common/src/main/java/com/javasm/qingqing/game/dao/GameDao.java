package com.javasm.qingqing.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.game.vo.HomeGameVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 技能所属游戏(Game)表数据库访问层
 *
 * @author makejava
 * @since 2025-11-29 15:59:45
 */
public interface GameDao extends BaseMapper<Game> {

    @Select("SELECT skill.skill_price as price," +
            "skill.uid,web.head_pic," +
            "web.nickname,game.`name` as game_name " +
            "FROM skill,game,web_user_info web " +
            "WHERE skill.game_id = game.id " +
            "AND skill.uid = web.uid " +
            "AND skill.game_id = #{id} limit 10")
    List<HomeGameVo> selectGameUserListByGid(Integer id);

    @Select("SELECT skill.skill_price as price," +
            "skill.uid,web.head_pic," +
            "web.nickname,game.`name` as game_name " +
            "FROM skill,game,web_user_info web " +
            "WHERE skill.game_id = game.id " +
            "AND skill.uid = web.uid " +
            "AND skill.skill_promotion > 0  " +
            "ORDER BY skill.order_times DESC," +
            "skill.skill_price ASC " +
            "LIMIT 10")
    List<HomeGameVo> selectGameUserHotList();

    @Select("SELECT skill.skill_price as price," +
            "skill.uid,web.head_pic," +
            "web.nickname,game.`name` as game_name, game.id as game_id " +
            "FROM skill,game,web_user_info web " +
            "WHERE skill.game_id = game.id " +
            "AND skill.uid = web.uid " +
            "AND skill.game_id in (${ids}) limit 10")
    List<HomeGameVo> selectGameUserListByGids(String ids);
}

