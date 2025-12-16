package com.javasm.qingqing.adminuser.vo;

import lombok.Data;

/**
 * @className: SearchVo
 * @author: gfs
 * @date: 2025/12/16 10:53
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class SearchVo {

    private Integer pageNum;
    private Integer pageSize;

    //id|昵称|用户名
    private String name;
    // 手机号|邮箱
    private String phone;
    //角色id
    private Integer id;
    //状态
    private Integer type;
    //创建时间
    private String start;
    private String end;


}
