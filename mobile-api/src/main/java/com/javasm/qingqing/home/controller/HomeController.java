package com.javasm.qingqing.home.controller;

import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.common.utils.ParameterUtils;
import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.home.service.HomeService;
import com.javasm.qingqing.game.vo.HomeGameVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @className: HomeController
 * @author: gfs
 * @date: 2025/11/29 16:00
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@RequestMapping("/home")
public class HomeController {
    @Resource
    HomeService homeService;

    @GetMapping("/hot/game/list")
    public R queryHotGameList(){
        List<Game> gameList = homeService.queryHotGameList();
        return R.ok(gameList);
    }

    @GetMapping("/hot/game/user/list/{id}")
    public R queryGameUserListByGid(@PathVariable Integer id){
        ParameterUtils.checkParameter(id);
        List<HomeGameVo> list = homeService.queryHotGameUserList(id);
        return R.ok(list);
    }

    @GetMapping("/game")
    public R queryHomeGame(){
        List<Game> list = homeService.getAllGameList();
        return R.ok(list);
    }
}
