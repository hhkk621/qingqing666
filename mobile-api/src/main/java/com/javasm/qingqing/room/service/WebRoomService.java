package com.javasm.qingqing.room.service;

import com.javasm.qingqing.room.entity.Room;
import com.javasm.qingqing.room.entity.RoomCategory;
import com.javasm.qingqing.room.vo.RoomVo;
import com.javasm.qingqing.room.vo.RoomCategoryVo;

import java.util.List;

/**
 * @className: WebRoomService
 * @author: gfs
 * @date: 2025/12/2 9:43
 * @version: 0.1
 * @since: jdk17
 * @description: 房间业务层接口
 */
public interface WebRoomService {
    List<RoomCategory> getRoomTypeList();

    List<Room> getRoomListByTypeId(Integer id);

}
