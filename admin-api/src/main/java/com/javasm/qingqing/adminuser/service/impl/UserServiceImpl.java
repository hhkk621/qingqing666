package com.javasm.qingqing.adminuser.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javasm.qingqing.adminuser.entity.AdminUser;
import com.javasm.qingqing.adminuser.service.AdminUserService;
import com.javasm.qingqing.adminuser.service.UserService;
import com.javasm.qingqing.adminuser.vo.SearchVo;
import com.javasm.qingqing.common.container.RedisKeys;
import com.javasm.qingqing.common.utils.NicknameUtil;
import com.javasm.qingqing.login.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @className: UserServiceImpl
 * @author: gfs
 * @date: 2025/12/16 10:52
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    AdminUserService adminUserService;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    LoginService loginService;
    @Autowired
    private TaskExecutor taskExecutor;

    @Resource
    private RedisTemplate<String,AdminUser> redisTemplate;


    @Override
    public PageInfo<AdminUser> listByPage(SearchVo vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        //查询数据
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        /**
         * SELECT * FROM admin_user WHERE
         * (uid LIKE '%x%' OR nickname LIKE '%x%' OR username LIKE '%x%')
         * AND
         * (phone LIKE '%yyyy%' OR email LIKE '%yyyy%')
         */
        if (!StringUtils.isEmpty(vo.getName())){
            String name = vo.getName();
            queryWrapper.and(i->{
                i.like(AdminUser::getUid,name);
                i.or();
                i.like(AdminUser::getUsername,name);
                i.or();
                i.like(AdminUser::getNickname,name);
            });
        }
        if (!StringUtils.isEmpty(vo.getPhone())){
            String phone = vo.getPhone();
            queryWrapper.and(i->{
                i.like(AdminUser::getPhone,phone);
                i.or();
                i.like(AdminUser::getEmail,phone);
            });
        }
        //角色
        Integer rid = vo.getId();
        if (rid != null){
            queryWrapper.and(i->{
                i.eq(AdminUser::getRoleId,rid);
            });
        }
        //状态
        Integer status = vo.getType();
        if (status !=null){
            queryWrapper.and(i->{
               i.eq(AdminUser::getIsvalid,status);
            });
        }
        //创建时间
        String start = vo.getStart();
        String end = vo.getEnd();
        if (!StringUtils.isEmpty(start)&&!StringUtils.isEmpty(end)){
            queryWrapper.and(i->{
                //首尾时间不准确
               //i.between(AdminUser::getRegTime,start,end);
                i.apply("date_format(reg_time,'%Y-%m-%d') BETWEEN {0} AND {1}",start,end);
            });
        }
        //排序
        queryWrapper.orderByDesc(AdminUser::getUid);
        List<AdminUser> list = adminUserService.list(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public void saveOrUpdate(AdminUser adminUser) {
        //看看是否修改了密码
        String password = adminUser.getPassword();
        if(!StringUtils.isEmpty(password)){
            String newPassword = passwordEncoder.encode(password);
            adminUser.setPassword(newPassword);
        }
        if (adminUser.getUid() == null){
            //uid 没有传，按照添加逻辑处理
            adminUser.setRegTime(new Date());
            //登录的人信息
            AdminUser loginUser = loginService.getLoginUser();
            adminUser.setCreateUid(loginUser.getUid());
            //随机分配头像
            String headPic = "http://cd.ray-live.cn/imgs/headpic/pic_%s.jpg";
            int i = ThreadLocalRandom.current().nextInt(0,580);
            adminUser.setHeadImg(String.format(headPic,i));
            //昵称
            String nickname = NicknameUtil.generateStylizedNickname();
            adminUser.setNickname(nickname);
        }else {
            adminUser.setRegTime(null);
            adminUser.setLoginTime(null);
        }
        adminUserService.saveOrUpdate(adminUser);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        taskExecutor.execute(()->{
            //获取 当前id对应的数据
            List<AdminUser> list = adminUserService.listByIds(List.of(ids));
            //redis中删除数据
            List<String> keyList = new ArrayList<>();
            for (AdminUser user : list){
                String key = String.format(RedisKeys.AdminUserLoginUname,user.getUsername());
                keyList.add(key);
            }
            //批量在redis中删除
            redisTemplate.delete(keyList);
        });
        //执行删除
        adminUserService.removeByIds(List.of(ids));
    }
}
