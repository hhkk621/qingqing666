package com.javasm.qingqing.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;

@Component
@ServerEndpoint("/test/socket")
public class TestSocket {



    //import jakarta.websocket.Session;
    //连接成功之后，执行的方法
    @OnOpen
    public void open(Session session){
        //如果想接收二进制数据，需要再单独设置一下二进制数据的缓冲区，
        //如果不设置是无法接收文件的
        session.setMaxBinaryMessageBufferSize(1024 * 1024 * 100);
        System.out.println("==========获取连接========="+session);
    }

    //连接关闭之后，执行的方法
    @OnClose
    public void close(Session session){
        System.out.println("----------关闭连接-----------"+session);
    }
    //产生异常之后，执行的
    @OnError
    public void error(Throwable throwable){
        try {
            throw throwable;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    //接收到客户端消息之后，执行
    //这个方法是主要的处理消息的方法
    @OnMessage
    public void replyMessage(Session session,String message) throws IOException {
        //处理文本消息
        //接收到消息之后，可以保存到数据库，也可以发送给其他客户端
        session.getBasicRemote().sendText("Echo："+message);
    }

    @OnMessage
    public void handleBinaryMessage(Session session,byte[] bytes) throws IOException {
        //处理二进制消息
        session.getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
    }
}
