package com.javasm.qingqing.webuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.webuser.dao.WebUserLoginLogDao;
import com.javasm.qingqing.webuser.entity.WebUserLoginLog;
import com.javasm.qingqing.webuser.service.WebUserLoginLogService;
import org.springframework.stereotype.Service;

/**
 * (WebUserLoginLog)表服务实现类
 *
 * @author makejava
 * @since 2025-11-29 11:56:01
 */
@Service("webUserLoginLogService")
public class WebUserLoginLogServiceImpl extends ServiceImpl<WebUserLoginLogDao, WebUserLoginLog> implements WebUserLoginLogService {

}

