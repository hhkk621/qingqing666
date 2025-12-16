package com.javasm.qingqing.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.game.vo.HomeGameVo;

import java.util.List;

/**
 * 技能所属游戏(Game)表服务接口
 *
 * @author makejava
 * @since 2025-11-29 15:59:45
 */
public interface GameService extends IService<Game> {

    List<Game> getHotGameList();

    List<HomeGameVo> queryHomeGameUserList(Integer id);

    List<HomeGameVo> queryHomeGameUserHot();

    List<HomeGameVo> getHotGameUserList(List<Game> list);
}

