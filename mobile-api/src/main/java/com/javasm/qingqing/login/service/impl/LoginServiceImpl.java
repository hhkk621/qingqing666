package com.javasm.qingqing.login.service.impl;

import com.javasm.qingqing.common.annotation.SaveToken;
import com.javasm.qingqing.common.annotation.WebLoginLog;
import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.common.utils.IpAddressUtil;
import com.javasm.qingqing.common.utils.MD5Util;
import com.javasm.qingqing.login.service.LoginService;
import com.javasm.qingqing.webuser.entity.WebUser;
import com.javasm.qingqing.webuser.entity.WebUserInfo;
import com.javasm.qingqing.webuser.entity.WebUserPossession;
import com.javasm.qingqing.webuser.service.WebUserInfoService;
import com.javasm.qingqing.webuser.service.WebUserService;
import com.javasm.qingqing.webuser.service.impl.WebUserPossessionServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @className: LoginServiceImpl
 * @author: gfs
 * @date: 2025/11/29 11:08
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    WebUserService webUserService;
    @Resource
    WebUserInfoService webUserInfoService;
    @Resource
    RedisTemplate<String,WebUser> redisTemplate;
    @Resource
    IpAddressUtil ipAddressUtil;
    @Autowired
    private WebUserPossessionServiceImpl webUserPossessionService;
    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    @WebLoginLog
    @SaveToken
    public WebUser doUnameLogin(String username, String password) {
        //先从Redis中获取用户信息 weuser:username:%s
        //考虑到Key会反复使用，新建一个常量类，统一管理Redis的Key
        String key = String.format(RedisKeys.WebUserName,username);
        //从Redis中获取数据
        WebUser webUser = redisTemplate.opsForValue().get(key);
        if (webUser != null){
            if (webUser.getUid() == null){
                throw new JavasmException(ExceptionEnum.User_Not_Found);
            }
            //说明已经查询到了数据
            //检查密码
            //如果密码不正确，抛出异常
            webUser.checkPassword(password);
            //刷新一下Redis的过期时间
            taskExecutor.execute(()->{
                redisTemplate.expire(key,15, TimeUnit.DAYS);
            });

            return webUser;
        }
        //Redis中获取不到数据了
        //根据用户名 查询用户信息
        WebUser loginUser = webUserService.getByUname(username);
        //如果用户信息是null ，说明数据库没有
        if (loginUser == null){
            taskExecutor.execute(()->{
                redisTemplate.opsForValue().set(key,new WebUser(),10,TimeUnit.MINUTES);
            });
            throw new JavasmException(ExceptionEnum.User_Not_Found);
        }
        //如果有用户信息，校验密码是否正确
        loginUser.checkPassword(password);
        //登录成功之后，存入Redis之前，要查询出WebUserInfo的信息
        WebUserInfo userInfo = webUserInfoService.getById(loginUser.getUid());
        if (userInfo != null){
            loginUser.setUserInfo(userInfo);
        }
        //登录成功了
        taskExecutor.execute(()->{
            redisTemplate.opsForValue().set(key,loginUser,15,TimeUnit.DAYS);
        });
        //返回用户信息
        return loginUser;
    }

    @Override
    @WebLoginLog
    @SaveToken
    public WebUser doUidLogin(String uid) {
        String key = String.format(RedisKeys.WebUserId,uid);
        WebUser webUser = redisTemplate.opsForValue().get(key);
        if (webUser != null){
            return webUser;
        }
        //假设缓存过期没有查询到
        WebUser loginUser = webUserService.getById(uid);
        if (loginUser != null){
            //用户详情
            WebUserInfo userInfo = webUserInfoService.getById(uid);
            loginUser.setUserInfo(userInfo);
            //用户财产
            WebUserPossession possession = webUserPossessionService.getById(uid);
            loginUser.setPossession(possession);
            //存到Redis
            redisTemplate.opsForValue().set(key,loginUser,15,TimeUnit.DAYS);
        }else {
            redisTemplate.opsForValue().set(key,new WebUser(),3,TimeUnit.MINUTES);
        }

        return loginUser;
    }


    @Override
    @Transactional
    @WebLoginLog//注册完成，自动登录，记录登录日志
    public WebUser doRegister(WebUser webUser) {
        //用户数据 单独拿出来
        String username = webUser.getUsername();
        String password = webUser.getPassword();
        String phone = webUser.getPhone();
        String nickname = webUser.getUserInfo().getNickname();
        //校验 用户名是否重复
        WebUser byUname = webUserService.getByUname(username);
        if (byUname != null){
            throw new JavasmException(ExceptionEnum.User_Exist);
        }
        //密码,如果密码加密，需要把用户传入的密码加密之后，重新放到对象中
        String md5password = MD5Util.encrypt(password);
        webUser.setPassword(md5password);
        //注册时间
        webUser.setRegTime(new Date());
        //状态--数据库中已经配置了默认值，但是等会要把数据存入Redis，不想二次查询数据库，所以配一个默认值
        webUser.setStatus(0);
        //保存webUser数据
        webUserService.save(webUser);
        //数据存储成功，就会有uid
        Integer uid = webUser.getUid();
        if (uid == null){
            throw new JavasmException(ExceptionEnum.SYSTEM_ERROR);
        }
        WebUserInfo userInfo = webUser.getUserInfo();
        userInfo.setUid(uid);
        //如果昵称是空的，生成一个
        if (StringUtils.isEmpty(nickname)){
            //用户没有传昵称，分配一个默认昵称
            userInfo.setNickname("虾仁"+uid);
        }
        //分配头像
        String path = "http://cd.ray-live.cn/imgs/headpic/pic_%s.jpg";
        //顾前不顾后
        int num = ThreadLocalRandom.current().nextInt(1, 580);
        userInfo.setHeadPic(String.format(path,num));
        //ip
        userInfo.setIpAddress(ipAddressUtil.getClientIp());
        //其他的属性
        userInfo.setVipGrade(1);
        userInfo.setFansNum(0);
        userInfo.setLoadGrade(1);
        userInfo.setGodStatus(0);
        userInfo.setLockoutStatus("1");
        //...
        //异步了
        webUserInfoService.saveOne(userInfo);
        //userInfo.insert();
        //添加用户财富信息
        WebUserPossession possession = new WebUserPossession();
        possession.setUid(uid);
        possession.setRichNum(0L);
        possession.setCharmNum(0L);
        //异步了
        WebUserPossession newProssession = possession.clone();
        taskExecutor.execute(()->{
            //假设，我需要在子线程中，修改对象中的数据
            //又不想让子线程中修改的数据，影响主线程中的对象
            //需要克隆
            newProssession.setRichNum(10L);//这里是10保存
            newProssession.insert();
        });
        //外面保持的时候，必须是0L
        webUser.setPossession(possession);
        //数据保存到redis
        /*new Thread(()->{
            String uname_key = String.format(RedisKeys.WebUserName,username);
            redisTemplate.opsForValue().set(uname_key,webUser,15,TimeUnit.DAYS);
            String uid_key = String.format(RedisKeys.WebUserId,uid);
            redisTemplate.opsForValue().set(uid_key,webUser,15,TimeUnit.DAYS);
        }).start();*/
        taskExecutor.execute(()->{
            String uname_key = String.format(RedisKeys.WebUserName,username);
            redisTemplate.opsForValue().set(uname_key,webUser,15,TimeUnit.DAYS);
            String uid_key = String.format(RedisKeys.WebUserId,uid);
            redisTemplate.opsForValue().set(uid_key,webUser,15,TimeUnit.DAYS);
        });
        return webUser;
    }

}
