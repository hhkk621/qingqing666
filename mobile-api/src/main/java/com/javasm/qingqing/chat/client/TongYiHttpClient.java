package com.javasm.qingqing.chat.client;

import com.javasm.qingqing.chat.client.vo.Image2ImageResult;
import com.javasm.qingqing.chat.client.vo.Image2ImageVo;
import jakarta.annotation.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: TongYiHttpClient
 * @author: gfs
 * @date: 2025/12/5 10:19
 * @version: 0.1
 * @since: jdk17
 * @description: 通义千问HTTP客户端，用于发送图像处理请求
 */
@Component
public class TongYiHttpClient {

    @Resource
    RestTemplate restTemplate;

    private static final String Image2ImageUrl =
            "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation";

    private static final String TongYiKey = System.getenv("DASHSCOPE_API_KEY");

    //message生成图片的文字描述
    //imageUrl在线图片的地址
    //modelName 模型的名称
    public List<String> sendImage2Image(String message, String imgUrl, String modelName) {
        //Header
        HttpHeaders headers = new HttpHeaders();
        //--header 'Content-Type: application/json' \
        headers.setContentType(MediaType.APPLICATION_JSON);
        //--header "Authorization: Bearer $DASHSCOPE_API_KEY" \
        headers.set("Authorization", "Bearer " + TongYiKey);
        //配置参数
        Image2ImageVo vo = new Image2ImageVo(message, imgUrl, modelName);
        //创建request对象，发送数据
        HttpEntity<Image2ImageVo> request = new HttpEntity<>(vo, headers);
        //发送请求，生成图片，同步生成，代码到这里会等待
        Image2ImageResult image2ImageResult =
                restTemplate.postForObject(Image2ImageUrl, request, Image2ImageResult.class);
        System.out.println(image2ImageResult);
        //根据响应的地址，获取图片集合
        List<Image2ImageResult.Content> contentList = image2ImageResult.getOutput().getChoices().get(0).getMessage().getContent();
        //筛选出图片结果的url集合
        List<String> urlList = contentList.stream()
                .filter(content -> content instanceof Image2ImageResult.ImageContent) // 过滤出ImageContent类型
                .map(content -> (Image2ImageResult.ImageContent) content) // 安全地转换为ImageContent类型
                .map(Image2ImageResult.ImageContent::getImage) // 提取image字段
                .collect(Collectors.toList());
        return urlList;
    }
}