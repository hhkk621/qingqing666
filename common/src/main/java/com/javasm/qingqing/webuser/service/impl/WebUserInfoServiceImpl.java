package com.javasm.qingqing.webuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.webuser.dao.WebUserInfoDao;
import com.javasm.qingqing.webuser.entity.WebUserInfo;
import com.javasm.qingqing.webuser.service.WebUserInfoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 陪玩用户表(WebUserInfo)表服务实现类
 *
 * @author makejava
 * @since 2025-11-29 11:40:38
 */
@Service("webUserInfoService")
public class WebUserInfoServiceImpl extends ServiceImpl<WebUserInfoDao, WebUserInfo> implements WebUserInfoService {

    @Override
    @Async
    public void saveOne(WebUserInfo userInfo) {
        super.save(userInfo);
    }
}

