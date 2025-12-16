package com.javasm.qingqing.chat.websocket;

/**
 * AI聊天WebSocket处理器
 * 处理用户与AI之间的实时聊天功能
 */

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.javasm.qingqing.chat.client.TongYiHttpClient;
import com.javasm.qingqing.chat.entity.ChatHistoryInfo;
import com.javasm.qingqing.chat.service.ChatHistoryInfoService;
import com.javasm.qingqing.chat.service.ChatHistoryService;
import com.javasm.qingqing.chat.vo.AiMessage;
import com.javasm.qingqing.chat.vo.ChatHistoryMessage;
import com.javasm.qingqing.common.constans.WebSocketAgreement;
import com.javasm.qingqing.common.utils.QiniuKodoUtil;
import com.javasm.qingqing.common.utils.SpringContextUtil;
import com.javasm.qingqing.webuser.entity.WebUserAi;
import com.javasm.qingqing.webuser.entity.WebUserInfo;
import com.javasm.qingqing.webuser.service.WebUserAiService;
import com.javasm.qingqing.webuser.service.WebUserInfoService;
import io.reactivex.Flowable;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@ServerEndpoint("/ai/textChat/{myuid}/{touid}")
public class AiChatSocket {
    //查询出，和我聊天的人的信息
    WebUserAi userAi = new WebUserAi();
    //聊天这个ai的具体详情
    WebUserInfo userInfo = new WebUserInfo();
    //两个人聊天记录的id
    private Integer chatId = 0;
    List<Message> msgList = new ArrayList<>();

    /**
     * 获取聊天历史服务实例
     *
     * @return ChatHistoryService 聊天历史服务实例
     */
    public ChatHistoryService getChatHistoryService() {
        return SpringContextUtil.getBean(ChatHistoryService.class);
    }

    /**
     * 获取聊天历史信息服务实例
     *
     * @return ChatHistoryInfoService 聊天历史信息服务实例
     */
    public ChatHistoryInfoService getChatHistoryInfoService() {
        return SpringContextUtil.getBean(ChatHistoryInfoService.class);
    }

    /**
     * WebSocket连接打开时的处理方法
     * 初始化AI聊天环境，加载用户信息和聊天历史
     *
     * @param myuid   当前用户的ID
     * @param touid   对方用户(AI)的ID
     * @param session WebSocket会话对象
     * @throws IOException IO异常
     */
    @OnOpen
    public void startAi(@PathParam("myuid") Integer myuid,
                        @PathParam("touid") Integer touid,
                        Session session) throws IOException {
        //设置合理的二进制消息最大大小限制 (100MB)
        session.setMaxBinaryMessageBufferSize(100 * 1024 * 1024);
        //设置文本消息最大大小限制 (1MB)
        session.setMaxTextMessageBufferSize(1024 * 1024);
        //进入聊天界面的时候，第一个触发的方法
        //根据uid，查询出用户的信息，touid 和我聊天的这个人的用户信息
        WebUserAiService aiUserService = SpringContextUtil.getBean(WebUserAiService.class);
        userAi = aiUserService.getById(touid);
        //查询个人详情信息
        WebUserInfoService infoUserService = SpringContextUtil.getBean(WebUserInfoService.class);
        userInfo = infoUserService.getById(touid);
        //开启对话，配置聊天对象,ai的人设
        String aiProm = "You are a helpful assistant.";
        if (userAi != null && userAi.getCharacter() != null) {
            aiProm = userAi.getCharacter();
        }
        //构建ai的角色对象
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(aiProm)
                .build();
        msgList.add(systemMsg);
        //查询两个人是否有聊天记录
        chatId = getChatHistoryService().queryIdByMyUidAndToUid(myuid, touid);
        if (chatId != null) {
            //根据聊天id 查询两人的具体聊天记录
            List<ChatHistoryInfo> historyInfoList =
                    getChatHistoryInfoService().queryListByChatId(chatId);
            if (!historyInfoList.isEmpty()) {
                //把聊天记录 发送给用户
                ChatHistoryMessage chatHistoryMessage =
                        new ChatHistoryMessage(WebSocketAgreement.Chat_History, historyInfoList);
                //json对象转换
                sendText(session, chatHistoryMessage);
            }
        }


    }

    /**
     * WebSocket连接关闭时的处理方法
     *
     * @param myuid   当前用户的ID
     * @param touid   对方用户(AI)的ID
     * @param session WebSocket会话对象
     */
    @OnClose
    public void endAi(@PathParam("myuid") Integer myuid,
                      @PathParam("touid") Integer touid,
                      Session session) {

    }

    /**
     * WebSocket发生错误时的处理方法
     *
     * @param throwable 异常对象
     */
    @OnError
    public void error(Throwable throwable) {
        throwable.printStackTrace();
    }

    //文本消息
    private static String question = "";
    /**
     * 接收并处理WebSocket文本消息
     * 处理用户提问，调用AI生成回答，并保存聊天记录
     *
     * @param myuid   当前用户的ID
     * @param touid   对方用户(AI)的ID
     * @param session WebSocket会话对象
     * @param json    前端发送的JSON格式消息
     * @throws NoApiKeyException      缺少API密钥异常
     * @throws InputRequiredException 输入必需异常
     */
    @OnMessage
    public void replyMessage(@PathParam("myuid") Integer myuid,
                             @PathParam("touid") Integer touid,
                             Session session, String json) throws NoApiKeyException, InputRequiredException {
        //把前端传入的json进行处理
        JSONObject jsonObject = JSONObject.parse(json);
        //问题
        Object questionObj = jsonObject.get("question");
        Object typeObj = jsonObject.get("type");
        if (questionObj != null && typeObj != null) {
            //转成问题字符串
            question = questionObj.toString();
            //当前的提问类型 1文字，2 图片
            if (typeObj.equals(1)){
                //文本聊天
                textChat(myuid,touid,session);
            }else if (typeObj.equals(2)){
                //图片生成,暂时不做处理
            }


        }
    }

    private void textChat(Integer myuid,Integer touid,Session session) throws NoApiKeyException, InputRequiredException {

        //服务端收到了客户端传入的问题
        //保存聊天记录
        getChatHistoryInfoService().saveHistory(chatId, myuid, question);
        //用户发送来的内容，只有在聊天记录中显示，才能让用户意识到已经发送成功了
        //把这个消息再返回给用户
        AiMessage myQuestion = new AiMessage(WebSocketAgreement.AI_Question, question, 2);
        sendText(session, myQuestion);
        //开始向AI提问  ↓
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(question)
                .build();
        msgList.add(userMsg);
        String modelName = "qwen3-max";
        if (userAi.getModel() != null) {
            modelName = userAi.getModel();
        }
        //组装完 用户的提问
        Generation gen = new Generation();
        GenerationParam param = GenerationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model(modelName)
                .messages(msgList)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
        Flowable<GenerationResult> result = gen.streamCall(param);
        //代码运行到这里，ai思考结束，回答开始
        //通知客户端，ai开始回答问题
        AiMessage aiMessageAnswerStart = new AiMessage(WebSocketAgreement.AI_Answer, "回答开始", 0);
        sendText(session, aiMessageAnswerStart);
        //循环显示答案内容
        StringBuilder saveAnswer = new StringBuilder();
        result.blockingForEach(data -> {
            //data中有完整的答案，json字符串
            String content = data.getOutput().getChoices().get(0).getMessage().getContent();
            //发送答案给客户端
            AiMessage aiMessageAnswer = new AiMessage(WebSocketAgreement.AI_Answer, content, 1);
            sendText(session, aiMessageAnswer);
            saveAnswer.append(content);
        });
        //循环发送结束
        //告诉客户端，回答终止了
        AiMessage aiMessageAnswerEnd = new AiMessage(WebSocketAgreement.AI_Answer, "回答结束", 2);
        sendText(session, aiMessageAnswerEnd);
        //保存聊天记录
        getChatHistoryInfoService().saveHistory(chatId, touid, saveAnswer.toString());
    }
    /**
     * 向客户端发送文本消息
     * 将对象序列化为JSON字符串并通过WebSocket会话发送
     *
     * @param session WebSocket会话对象
     * @param object  需要发送的对象
     */
    public void sendText(Session session, Object object) {
        try {
            String msg = JSON.toJSONString(object);
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //处理二进制文件
    @OnMessage
    public void onMessage(@PathParam("myuid") Integer myuid,
                          @PathParam("touid") Integer touid,
                          Session session,byte[] bytes){
        //预先预定好，所有的文件上传，都要配置一个头信息，以此区分不同的文件类型
        //进入的二进制数据，必须长度 >6
        if (bytes == null || bytes.length < 6){
            return;
        }
        
        //检查消息大小，防止内存溢出
        if (bytes.length > 10 * 1024 * 1024) { // 限制为10MB
            try {
                session.getBasicRemote().sendText("{\"error\":\"File size too large, maximum 10MB allowed\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        //解析头信息
        String header = new String(bytes,0,6, StandardCharsets.UTF_8);
        if ("IMAGE:".equals(header)){
            //是图片，根据图片的方案，继续处理
            byte[] imageData = Arrays.copyOfRange(bytes,6,bytes.length);
            image2image(myuid,touid,session,imageData);
        }
    }

    public TongYiHttpClient getAiHttpClient(){
        return SpringContextUtil.getBean(TongYiHttpClient.class);
    }
    //图片生成图片的方法
    private void image2image(Integer myuid, Integer touid, Session session, byte[] imageData) {
        //获取客户端对象，RestTemplate
        //获取message，用户生成图片的文字描述
        //图片的在线地址，模型名称
        String modelName = "qwen-image-edit-plus";
        String questionUrl = QiniuKodoUtil.uploadAiQuestionChatImage(imageData);
        //保存聊天记录---我说的话
        getChatHistoryInfoService().saveHistory(chatId,myuid,question);
        AiMessage aiMessage = new AiMessage(WebSocketAgreement.AI_Question,question,2);
        sendText(session,aiMessage);
        //保存我发的图片
        getChatHistoryInfoService().saveHistory(chatId,myuid,questionUrl);
        //答案保存之后，把信息发送给用户
        AiMessage questionImg = new AiMessage(WebSocketAgreement.AI_Question_Image,questionUrl,2);
        sendText(session,questionImg);
        //告诉前端，开始生成生成图片--用来体现页面，播放等待的动画或者提示
        AiMessage startAiImage = new AiMessage(WebSocketAgreement.AI_Answer_Image,"开始生成图片",0);
        sendText(session,startAiImage);
        //ai生成图片
        List<String> urlList = getAiHttpClient().sendImage2Image(question, questionUrl, modelName);
        //七牛云可以直接保存在线图片
        List<String> saveList = new ArrayList<>();
        urlList.forEach(url ->{
            //url有效期是24小时
            String saveUrl = QiniuKodoUtil.uploadAiAnswerChatImage(url);
            saveList.add(saveUrl);
            //输出图片
            AiMessage imageUrlMessage = new AiMessage(WebSocketAgreement.AI_Answer_Image,saveUrl,1);
            sendText(session,imageUrlMessage);
        });
        //图片循环结束--用来告诉客户端，图片生成结束了
        AiMessage endAiImage = new AiMessage(WebSocketAgreement.AI_Answer_Image,"图片生成结束",2);
        sendText(session,endAiImage);

        //把ai生成的图片答案，保存聊天记录
        getChatHistoryInfoService().saveHistory(chatId,touid,saveList);

    }
}
