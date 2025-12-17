package com.javasm.qingqing.adminuser.vo;

import lombok.Data;

import java.util.List;
@Data
public class RoleMenuAuthVo {
    private Integer rid;
    private List<Integer> mids;
}