package com.javasm.qingqing.home.service.impl;

import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.game.service.GameService;
import com.javasm.qingqing.home.service.HomeService;
import com.javasm.qingqing.game.vo.HomeGameVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @className: HomeServiceImpl
 * @author: gfs
 * @date: 2025/11/29 16:00
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    GameService gameService;

    @Override
    public List<Game> queryHotGameList() {
        List<Game>  gameList = gameService.getHotGameList();
        return gameList;
    }

    @Override
    public List<HomeGameVo> queryHotGameUserList(Integer id) {
        List<HomeGameVo> list;
        if (id == -1){
            list = gameService.queryHomeGameUserHot();
        }else {
            //根据游戏id 查询信息
            list = gameService.queryHomeGameUserList(id);
        }
        return list;
    }

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<Game> getAllGameList() {
        //去Redis中查询数据
        String key = RedisKeys.HomeHotList;
        //数据是list ，但是不想存储成redislist类型，整存整取
        Object o = redisTemplate.opsForValue().get(key);
        if(o != null){
            return (List<Game>) o;
        }
        //说明缓存没有数据，数据库查询
        //查询游戏以列表
        List<Game> list = gameService.getHotGameList();
        //根据游戏列表，把所有的游戏 用户信息 查询到
        List<HomeGameVo> homeGameVoList = gameService.getHotGameUserList(list);
        //手工创建一个 id = -1的推荐游戏
        Game gameHot = new Game();
        gameHot.setId(-1);
        gameHot.setName("人气推荐");
        gameHot.setIcon("http://cd.ray-live.cn/imgs/game/jx.png");
        List<HomeGameVo> gameVoList = gameService.queryHomeGameUserHot();
        gameHot.setGameSkillList(gameVoList);
        list.add(0,gameHot);
        //循环游戏列表，寻找对应的陪玩人员信息
        list.forEach(game->{
            if (game.getId() != -1){
                //空的集合，等会存储当前循环的游戏，都有哪些人员
                List<HomeGameVo> gameSkillList = new ArrayList<>();
                homeGameVoList.forEach(homeGameVo -> {
                    if (game.getId().equals(homeGameVo.getGameId())){
                        //在大的集合中，找到当前游戏的陪玩人员信息，收集起来
                        gameSkillList.add(homeGameVo);
                    }
                });
                //陪玩列表，存入游戏对象
                game.setGameSkillList(gameSkillList);
            }
        });
        //数据组装完成，存入Redis
        redisTemplate.opsForValue().set(key,list,2, TimeUnit.HOURS);
        return list;
    }
}
