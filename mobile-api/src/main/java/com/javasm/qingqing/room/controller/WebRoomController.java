package com.javasm.qingqing.room.controller;

import com.javasm.qingqing.common.annotation.Auth;
import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.common.utils.ParameterUtils;
import com.javasm.qingqing.room.entity.Room;
import com.javasm.qingqing.room.entity.RoomCategory;
import com.javasm.qingqing.room.service.WebRoomService;
import com.javasm.qingqing.room.vo.RoomVo;
import com.javasm.qingqing.room.vo.RoomCategoryVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: WebRoomController
 * @author: gfs
 * @date: 2025/12/2 9:41
 * @version: 0.1
 * @since: jdk17
 * @description: 房间模块控制器
 */
@RestController
@RequestMapping("/room")
public class WebRoomController {

    @Resource
    WebRoomService webRoomService;

    //大厅分类列表--1级
    @GetMapping("/type/list")
    @Auth
    public R queryRoomTypeList(){
        List<RoomCategory> typeList = webRoomService.getRoomTypeList();
        return R.ok(typeList);
    }

    @Auth
    @GetMapping("/query/{id}")
    public R queryRoomListByTypeId(@PathVariable Integer id){
        ParameterUtils.checkParameter(id);
        List<Room> list = webRoomService.getRoomListByTypeId(id);
        return R.ok(list);
    }

}
