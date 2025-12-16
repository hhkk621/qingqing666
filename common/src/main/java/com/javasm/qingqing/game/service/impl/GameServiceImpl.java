package com.javasm.qingqing.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.game.dao.GameDao;
import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.game.service.GameService;
import com.javasm.qingqing.game.vo.HomeGameVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 技能所属游戏(Game)表服务实现类
 *
 * @author makejava
 * @since 2025-11-29 15:59:45
 */
@Service("gameService")
public class GameServiceImpl extends ServiceImpl<GameDao, Game> implements GameService {

    @Resource
    GameDao gameDao;

    @Override
    public List<Game> getHotGameList() {
        LambdaQueryWrapper<Game> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Game::getIshot,1);
        queryWrapper.orderByAsc(Game::getSort);
        List<Game> list = list(queryWrapper);
        return list;
    }

    @Override
    public List<HomeGameVo> queryHomeGameUserList(Integer id) {
        return gameDao.selectGameUserListByGid(id);
    }

    @Override
    public List<HomeGameVo> queryHomeGameUserHot() {

        return gameDao.selectGameUserHotList();
    }

    @Override
    public List<HomeGameVo> getHotGameUserList(List<Game> list) {

        //"1,2,3,4"
        String ids = list.stream().map(game -> String.valueOf(game.getId())).collect(Collectors.joining(","));
        //减少数据库访问次数，不能使用for循环访问
        List<HomeGameVo>  gameVoList = gameDao.selectGameUserListByGids(ids);
        return gameVoList;
    }
}

