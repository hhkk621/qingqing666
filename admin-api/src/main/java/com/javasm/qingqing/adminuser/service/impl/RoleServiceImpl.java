package com.javasm.qingqing.adminuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminRole;
import com.javasm.qingqing.adminuser.service.AdminRoleService;
import com.javasm.qingqing.adminuser.service.RoleService;
import com.javasm.qingqing.adminuser.vo.SearchVo;
import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.common.utils.ParameterUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;



@Service("myRoleService")
public class RoleServiceImpl implements RoleService {



    @Resource
    AdminRoleService adminRoleService;
    //建议把role的集合放到redis中
    //因为数据量太小，可以存储到JVM内存中
    private static List<AdminRole> roleList = new ArrayList<>();
    private static Long roleListExpTime = 1000L * 60 * 60 * 2;
    private static Long roleListSaveTime = 0L;

    @Override
    public List<AdminRole> roleListAll() {
        //判断 roleList是否过期
        if (System.currentTimeMillis() - roleListSaveTime > roleListExpTime){
            roleList = new ArrayList<>();
        }
        if (roleList.isEmpty()){
            roleList = adminRoleService.list();
            roleListSaveTime = System.currentTimeMillis();
        }
        return roleList;
    }

    //分页查询所有的角色
    @Override
    public PageInfo<AdminRole> page(SearchVo searchVo, Integer pageNum, Integer pageSize) {
        //配置分页对象
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
        if (searchVo != null){
            if(searchVo.getId() != null){
                queryWrapper.eq(AdminRole::getRid, searchVo.getId());
            }
            if (StringUtils.hasText(searchVo.getName())) {
                // 正确写法：将OR条件用括号包裹
                queryWrapper.and(wrapper ->
                        wrapper.like(AdminRole::getName, searchVo.getName())
                                .or()
                                .like(AdminRole::getCode, searchVo.getName())
                );
            }
        }
        //排除一下超级管理员
        queryWrapper.ne(AdminRole::getRid, 1);
        //根据rid进行逆序排序 新增加的让它在上面显示
        queryWrapper.orderByDesc(AdminRole::getRid);
        List<AdminRole> list = adminRoleService.list(queryWrapper);
        return new PageInfo<>(list);
    }

    //保存或更新
    @Override
    public void saveOrUpdate(AdminRole role) {
        ParameterUtils.checkParameter(role);

        if (role.getRid() == null || role.getRid() <= 0){
            //新增情况判断角色名和code值不能够重复
            ParameterUtils.checkParameter(role.getName(), role.getCode());
            LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AdminRole::getName, role.getName());
            queryWrapper.or();
            queryWrapper.eq(AdminRole::getCode, role.getCode());
            if (adminRoleService.count(queryWrapper)  > 0){
                throw new JavasmException(ExceptionEnum.RoleNameOrRoleCodeAlreadyExists);
            }
        }

        adminRoleService.saveOrUpdate(role);
    }

    @Override
    //开启事务
    @Transactional
    public void deleteByIds(Integer[] ids) {
        adminRoleService.removeByIds(List.of(ids));
    }
}
