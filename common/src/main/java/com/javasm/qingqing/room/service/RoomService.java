package com.javasm.qingqing.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.room.entity.Room;

import java.util.List;

/**
 * 陪玩直播大厅(Room)表服务接口
 *
 * @author makejava
 * @since 2025-12-02 09:41:26
 */
public interface RoomService extends IService<Room> {

    List<Room> getListByCid(Integer id);

    List<Room> getHotRoomList();
}

