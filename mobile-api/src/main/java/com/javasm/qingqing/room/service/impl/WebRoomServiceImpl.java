package com.javasm.qingqing.room.service.impl;

import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.room.entity.Room;
import com.javasm.qingqing.room.entity.RoomCategory;
import com.javasm.qingqing.room.service.RoomCategoryService;
import com.javasm.qingqing.room.service.RoomService;
import com.javasm.qingqing.room.service.WebRoomService;
import com.javasm.qingqing.room.vo.RoomVo;
import jakarta.annotation.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: WebRoomServiceImpl
 * @author: gfs
 * @date: 2025/12/2 9:43
 * @version: 0.1
 * @since: jdk17
 * @description: 房间业务层实现类
 */
@Service
public class WebRoomServiceImpl implements WebRoomService {

    @Resource
    RoomCategoryService roomCategoryService;
    @Resource
    RoomService roomService;

    @Resource
    RestTemplate restTemplate;

    private static final String BASE_URL = "http://127.0.0.1:8088";

    @Override
    public List<RoomCategory> getRoomTypeList() {
        List<RoomCategory>  list = roomCategoryService.queryHotRoomCategory();
        //TODO: 添加缓存
        return list;
    }

    @Override
    public List<Room> getRoomListByTypeId(Integer id) {
        List<Room> list;
        if (id == -1){
            list = roomService.getHotRoomList();
        }else {
            list = roomService.getListByCid(id);
        }

        return list;
    }

}
