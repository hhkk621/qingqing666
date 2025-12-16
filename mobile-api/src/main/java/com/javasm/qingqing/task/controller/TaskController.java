package com.javasm.qingqing.task.controller;

import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.task.service.TaskService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: TaskController
 * @author: gfs
 * @date: 2025/12/8 10:38
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    TaskService taskService;

    @GetMapping("/start/{id}")
    public R start(@PathVariable Integer id){
        taskService.start(id);
        return R.ok();
    }
    @GetMapping("/stop/{id}")
    public R stop(@PathVariable Integer id){
        taskService.stop(id);
        return R.ok();
    }
}
