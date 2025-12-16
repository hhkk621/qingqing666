package com.javasm.qingqing.chat.client.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * 图片到图片的响应结果类
 * 用于接收和解析AI图像编辑接口返回的数据
 */
@Data
@NoArgsConstructor
public class Image2ImageResult {
    
    /**
     * 输出结果
     */
    private Output output;
    
    /**
     * 使用情况统计
     */
    private Usage usage;
    
    /**
     * 请求ID
     */
    private String request_id;
    
    /**
     * 输出结果内部类
     */
    @Data
    public static class Output {
        /**
         * 选择结果列表
         */
        private List<Choice> choices;
    }
    
    /**
     * 选择结果内部类
     */
    @Data
    public static class Choice {
        /**
         * 结束原因
         */
        private String finish_reason;
        
        /**
         * 消息内容
         */
        private Message message;
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
        @JsonDeserialize(using = ContentDeserializer.class)
        private List<Content> content;
    }
    
    /**
     * 内容项基类
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
     * 内容反序列化器
     */
    public static class ContentDeserializer extends JsonDeserializer<List<Content>> {
        @Override
        public List<Content> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectCodec codec = jsonParser.getCodec();
            JsonNode node = codec.readTree(jsonParser);
            
            if (node.isArray()) {
                List<Content> contentList = new java.util.ArrayList<>();
                for (JsonNode contentNode : node) {
                    if (contentNode.has("image")) {
                        ImageContent imageContent = new ImageContent();
                        imageContent.setImage(contentNode.get("image").asText());
                        contentList.add(imageContent);
                    } else {
                        contentList.add(new Content());
                    }
                }
                return contentList;
            }
            
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * 使用情况统计内部类
     */
    @Data
    public static class Usage {
        /**
         * 图片宽度
         */
        private Integer width;
        
        /**
         * 图片数量
         */
        private Integer image_count;
        
        /**
         * 图片高度
         */
        private Integer height;
    }
}