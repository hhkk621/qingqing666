package com.javasm.qingqing.room.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.javasm.qingqing.common.utils.SpringContextUtil;
import com.javasm.qingqing.room.vo.MessageVo;
import com.javasm.qingqing.webuser.entity.WebUserInfo;
import com.javasm.qingqing.webuser.service.WebUserInfoService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/room/multiuser/{roomId}/{uid}")
public class RoomSocket {

    //被观察者  Map<roomId,Set<当前房间里，所有人的Session信息>>
    private static Map<String, Set<Session>> roomMap = new ConcurrentHashMap<>(8);

    @OnOpen
    public void enterRoom(@PathParam("roomId") String roomId,
                          @PathParam("uid") String uid,
                          Session session){
        //每次连接进来的新用户，新的客户端，session对象都是新的。
        //并且连接不断开的情况下，session对象是不变的
        //每次用户进入房间，都会默认执行这个方法
        //当前房间，所有的Session集合
        Set<Session> set = roomMap.get(roomId);
        if (set == null){
            //内部使用ReentrantLock 和 复制比数组机制，所有的修改都是线程安全的
            set = new CopyOnWriteArraySet<>();
            //set不可能是null
            roomMap.put(roomId,set);
        }
        set.add(session);
    }

    @OnClose
    public void closeRoom(@PathParam("roomId") String roomId,
                          @PathParam("uid") String uid,
                          Session session){
        //离开了
        if (roomMap.containsKey(roomId)){
            roomMap.get(roomId).remove(session);
        }
    }
    @OnMessage
    public void replyMessage(@PathParam("roomId") String roomId,
                             @PathParam("uid") String uid,
                             Session session,String message){
        //message  json 对象
        String msg = "";
        try {
            JSONObject jsonObject = JSONObject.parse(message);
            Object str = jsonObject.get("msg");
            if (str != null){
                msg = str.toString();//用户传入的消息
            }
        } catch (Exception e) {
            //如果用户传入的不是json数据，可能产生异常
            e.printStackTrace();
        }
        //获取到用户信息
        WebUserInfo userInfo = getWebUserInfoService().getById(uid);
        String jsonMessage = "";
        if (userInfo != null){
            //尽量不要传输无用的信息
            //、需要协议号--通过协议号来区分当前返回的信息是做什么的
            MessageVo vo = new MessageVo();
            vo.setAgreement(1001);
            vo.setPic(userInfo.getHeadPic());
            vo.setNickname(userInfo.getNickname());
            vo.setUid(userInfo.getUid());
            vo.setMessage(msg);
            jsonMessage = JSON.toJSONString(vo);
        }
        sendMessage(roomId, jsonMessage);
    }

    private void sendMessage(String roomId, String jsonMessage) {
        //获取当前房间里所有人
        Set<Session> sessionSet = roomMap.get(roomId);
        try {
            if (sessionSet != null){
                for (Session s : sessionSet){
                    s.getBasicRemote().sendText(jsonMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //这里通过@Resource可能无法获取Bean对象，所以 通过工具获取
    private WebUserInfoService getWebUserInfoService(){
        return SpringContextUtil.getBean(WebUserInfoService.class);
    }
}
