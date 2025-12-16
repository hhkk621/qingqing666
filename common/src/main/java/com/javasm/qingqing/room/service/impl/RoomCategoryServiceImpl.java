package com.javasm.qingqing.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.room.dao.RoomCategoryDao;
import com.javasm.qingqing.room.entity.RoomCategory;
import com.javasm.qingqing.room.service.RoomCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 陪玩直播大厅分类(RoomCategory)表服务实现类
 *
 * @author makejava
 * @since 2025-12-02 09:46:18
 */
@Service("roomCategoryService")
public class RoomCategoryServiceImpl extends ServiceImpl<RoomCategoryDao, RoomCategory> implements RoomCategoryService {

    @Override
    public List<RoomCategory> queryHotRoomCategory() {
        LambdaQueryWrapper<RoomCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoomCategory::getParentId,0);
        queryWrapper.orderByAsc(RoomCategory::getCategoryId);
        return list(queryWrapper);
    }
}

