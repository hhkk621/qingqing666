package com.javasm.qingqing.webuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.webuser.entity.WebUser;

/**
 * (WebUser)表服务接口
 *
 * @author makejava
 * @since 2025-11-29 11:13:15
 */
public interface WebUserService extends IService<WebUser> {

    WebUser getByUname(String username);
}

