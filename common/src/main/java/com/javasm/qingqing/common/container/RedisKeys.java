package com.javasm.qingqing.common.container;

/**
 * @className: RedisKeys
 * @author: gfs
 * @date: 2025/11/29 11:25
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
public interface RedisKeys {
    //用户名查询用户信息：webuser:username:用户名
    String WebUserName = "webuser:username:%s";
    String WebUserId = "webuser:uid:%s";
    //每日登录记录：webuser:login:log:uid:2025-11-11
    String WebLoginLog = "webuser:login:log:%s:%s";

    String NewsDayList = "news:day:%s";

    String HomeHotList = "home:game:hot:list";

    String AdminUserLoginUname = "adminuser:login:uname:%s";

    String AdminRoleListAll = "adminrole:list:all";

}
