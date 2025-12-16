package com.javasm.qingqing.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.room.dao.RoomDao;
import com.javasm.qingqing.room.entity.Room;
import com.javasm.qingqing.room.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 陪玩直播大厅(Room)表服务实现类
 *
 * @author makejava
 * @since 2025-12-02 09:41:26
 */
@Service("roomService")
public class RoomServiceImpl extends ServiceImpl<RoomDao, Room> implements RoomService {

    @Override
    public List<Room> getListByCid(Integer id) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getCategoryL1,id);
        queryWrapper.orderByAsc(Room::getId);
        return list(queryWrapper);
    }

    @Override
    public List<Room> getHotRoomList() {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getPush,2);
        queryWrapper.orderByAsc(Room::getId);
        return list(queryWrapper);
    }
}

