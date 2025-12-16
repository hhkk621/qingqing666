package com.javasm.qingqing.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javasm.qingqing.room.entity.RoomCategory;

import java.util.List;

/**
 * 陪玩直播大厅分类(RoomCategory)表服务接口
 *
 * @author makejava
 * @since 2025-12-02 09:46:18
 */
public interface RoomCategoryService extends IService<RoomCategory> {

    List<RoomCategory> queryHotRoomCategory();
}

