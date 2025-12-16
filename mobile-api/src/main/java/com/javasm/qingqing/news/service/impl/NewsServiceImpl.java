package com.javasm.qingqing.news.service.impl;

import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.news.service.NewsService;
import com.javasm.qingqing.news.vo.NewsResult;
import com.javasm.qingqing.news.vo.NewsVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @className: NewsServiceImpl
 * @author: gfs
 * @date: 2025/12/3 14:53
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    RestTemplate restTemplate;

    private static final String key = "b8726ecc2f74b3d9f57d808c95b01b19";
    private static final String esportsUrl = "https://apis.tianapi.com/esports/index";

    @Resource
    RedisTemplate<String,NewsVo> redisTemplate;

    @Override
    public List<NewsVo> syncNewsList() {
        //同步新闻列表
        String url = esportsUrl+"?key="+key+"&num=50";
        NewsResult newsResult = restTemplate.getForObject(url, NewsResult.class);
        if (newsResult !=null && newsResult.getCode().equals(200)){
            //把数据存入Redis
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
            String today = simpleDateFormat.format(new Date());
            String redisKey = String.format(RedisKeys.NewsDayList,today);
            List<NewsVo> newslist = newsResult.getResult().getNewslist();
            redisTemplate.opsForList().rightPushAll(redisKey,newslist);
            //给个过期时间
            redisTemplate.expire(redisKey,1, TimeUnit.HOURS);
            return newslist;
        }
        return null;

    }

    @Override
    public List<NewsVo> list() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        String today = simpleDateFormat.format(new Date());
        String redisKey = String.format(RedisKeys.NewsDayList,today);
        List<NewsVo> list = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (list == null || list.isEmpty()){
            list = syncNewsList();
        }
        return list;
    }
}
