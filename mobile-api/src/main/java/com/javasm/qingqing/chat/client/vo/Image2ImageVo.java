package com.javasm.qingqing.chat.client.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片到图片的请求VO类
 * 用于封装调用AI图像编辑接口所需的数据
 */
@Data
@NoArgsConstructor
public class Image2ImageVo {
    
    /**
     * 构造方法
     * 
     * @param message 文本消息内容，将被设置为content.text
     * @param imgUrl 图片URL，将被设置为content.image
     * @param modelName 模型名称，将被设置为model属性
     */
    public Image2ImageVo(String message, String imgUrl, String modelName) {
        // 设置模型名称
        this.model = modelName;
        
        // 创建输入参数
        this.input = new Input();
        
        // 创建消息列表
        List<Message> messages = new ArrayList<>();
        Message msg = new Message();
        msg.role = "user"; // 默认角色设置为"user"
        
        // 创建内容列表
        List<Content> contents = new ArrayList<>();
        
        // 添加图片内容（如果提供了图片URL）
        if (imgUrl != null && !imgUrl.isEmpty()) {
            ImageContent imageContent = new ImageContent();
            imageContent.setImage(imgUrl);
            contents.add(imageContent);
        }
        
        // 添加文本内容（如果提供了文本消息）
        if (message != null && !message.isEmpty()) {
            TextContent textContent = new TextContent();
            textContent.setText(message);
            contents.add(textContent);
        }
        
        // 设置内容列表到消息中
        msg.content = contents;
        // 将消息添加到消息列表中
        messages.add(msg);
        // 设置消息列表到输入参数中
        this.input.messages = messages;
        
        // 创建默认参数配置
        this.parameters = new Parameters();
        this.parameters.n = 2; // 默认生成2张图片
        this.parameters.prompt_extend = true; // 默认启用提示词扩展
        this.parameters.watermark = false; // 默认不添加水印
        this.parameters.negative_prompt = ""; // 默认空的负面提示词
    }

    /**
     * 模型名称
     */
    private String model;

    /**
     * 输入参数
     */
    private Input input;

    /**
     * 其他参数配置
     */
    private Parameters parameters;

    /**
     * 输入参数内部类
     */
    @Data
    public static class Input {
        /**
         * 消息列表
         */
        private List<Message> messages;
    }

    /**
     * 参数配置内部类
     */
    @Data
    public static class Parameters {
        /**
         * 生成图片数量
         */
        private Integer n;
        
        /**
         * 负面提示词
         */
        private String negative_prompt;
        
        /**
         * 是否扩展提示词
         */
        private boolean prompt_extend;
        
        /**
         * 是否添加水印
         */
        private boolean watermark;
    }

    /**
     * 消息内部类
     */
    @Data
    public static class Message {
        /**
         * 角色
         */
        private String role;
        
        /**
         * 内容列表
         */
        private List<Content> content;
    }

    /**
     * 内容项基类
     * 根据JSON结构，实际使用时会创建其子类ImageContent或TextContent
     */
    @Data
    public static class Content {
        // 基类，不包含具体字段
    }
    
    /**
     * 图片内容项
     */
    @Data
    public static class ImageContent extends Content {
        /**
         * 图片URL
         */
        private String image;
    }
    
    /**
     * 文本内容项
     */
    @Data
    public static class TextContent extends Content {
        /**
         * 文本内容
         */
        private String text;
    }
}