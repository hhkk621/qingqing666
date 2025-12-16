package com.javasm.qingqing.webuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.webuser.entity.WebUserInfo;

/**
 * 陪玩用户表(WebUserInfo)表服务接口
 *
 * @author makejava
 * @since 2025-11-29 11:40:38
 */
public interface WebUserInfoService extends IService<WebUserInfo> {

    void saveOne(WebUserInfo userInfo);
}

