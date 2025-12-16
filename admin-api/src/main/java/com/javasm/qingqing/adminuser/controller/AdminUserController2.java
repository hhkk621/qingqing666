package com.javasm.qingqing.adminuser.controller;

import com.javasm.qingqing.common.exception.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user2")
@Slf4j
public class AdminUserController2 {

    @GetMapping("/f1")
    public R f1(){
        log.debug("-------------------我是Debug");
        log.info("-------------------我是Info");
        log.warn("-------------------我是警告");
        log.error("-------------------我是Error!!!!");
        return R.ok();
    }
}
