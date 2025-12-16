package com.javasm.qingqing.webuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.webuser.dao.WebUserDao;
import com.javasm.qingqing.webuser.entity.WebUser;
import com.javasm.qingqing.webuser.service.WebUserService;
import org.springframework.stereotype.Service;

/**
 * (WebUser)表服务实现类
 *
 * @author makejava
 * @since 2025-11-29 11:13:15
 */
@Service("webUserService")
public class WebUserServiceImpl extends ServiceImpl<WebUserDao, WebUser> implements WebUserService {

    @Override
    public WebUser getByUname(String username) {
        LambdaQueryWrapper<WebUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WebUser::getUsername,username);
        WebUser webUser = getOne(queryWrapper);
        return webUser;
    }
}

