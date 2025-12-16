package com.javasm.qingqing.home.service;

import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.game.vo.HomeGameVo;

import java.util.List;

/**
 * @className: HomeService
 * @author: gfs
 * @date: 2025/11/29 16:00
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface HomeService {
    List<Game> queryHotGameList();

    List<HomeGameVo> queryHotGameUserList(Integer id);

    List<Game> getAllGameList();
}
