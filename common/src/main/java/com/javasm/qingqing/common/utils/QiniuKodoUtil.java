package com.javasm.qingqing.common.utils;

import com.alibaba.fastjson2.JSON;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class QiniuKodoUtil {

    private static Logger logger = LogManager.getLogger(QiniuKodoUtil.class);

    private static final String accessKey = "t0oUPjPwjqQiS2EM3vm5FIIGI8InWKOOXeNnFpZg";
    private static final String secretKey = "66Hd-TKVVbwRrHSTtiQqE8i8L-VOsUaJ1iWTO8rv";
    //空间名称
    private static final String bucketName = "javasmcd";
    //域名地址
    @Getter
    private static final String domain = "http://cd.ray-live.cn/";

    /**
     * 获取七牛token，返回给客户端
     *
     * @return
     */
    public static String getToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        return auth.uploadToken(bucketName);
    }




    // 新增方法：下载图片字节流
    private static byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    // 新增方法：获取文件扩展名
    private static String getFileExtension(String url) {
        try {
            String path = new URL(url).getPath();
            int dotIndex = path.lastIndexOf('.');
            return (dotIndex == -1) ? "" : path.substring(dotIndex);
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public static String uploadBytes(byte[] data, String key) {
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);

        String upToken = getToken();
        try {
            Response response = uploadManager.put(data, key, upToken);
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            logger.info("上传成功：{}", putRet.key);
            return domain + putRet.key; // 注意这里使用key而不是hash
        } catch (QiniuException ex) {
            logger.error("上传失败：{}", ex.response.toString());
            return null;
        }
    }

    public static String uploadAudio(byte[] audio) {
        if (audio == null) {
            return null;
        }
        String key = "audio/" + RandomUtil.getFileName("test.wav");
        return uploadBytes(audio, key);
    }


    public static String upload(String filePath) {
        return upload(new File(filePath));
    }

    public static String upload(File file) {
        //构造一个带指定 Region 对象的配置类
        /**
         * 华东	Region.region0(), Region.huadong()
         * 华北	Region.region1(), Region.huabei()
         * 华南	Region.region2(), Region.huanan()
         * 北美	Region.regionNa0(), Region.beimei()
         * 东南亚	Region.regionAs0(), Region.xinjiapo()
         */
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //上传对象
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;//file.getName();//null;
        String upToken = getToken();
        try {
            Response response = uploadManager.put(file, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            logger.info(putRet.key);
            logger.info(domain + putRet.hash);
            return domain + putRet.hash;
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.error(r.toString());
            try {
                logger.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }

    public static void main(String[] args) {

    }

    public static String uploadAdminHeader(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String key = "admin_header/" + RandomUtil.getFileName(Objects.requireNonNull(file.getOriginalFilename()));
            return uploadBytes(bytes, key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String uploadAiQuestionChatImage(byte[] data){
        String key = "ai_question/"+RandomUtil.getFileName(".png");
        return uploadBytes(data,key);
    }
    public static String uploadAiAnswerChatImage(byte[] data){
        String key = "ai_answer/"+RandomUtil.getFileName(".png");
        return uploadBytes(data,key);
    }
    public static String uploadAiAnswerChatImage(String url){
        try {
            byte[] bytes = downloadImage(url);
            return uploadAiAnswerChatImage(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getRandomHeader() {
        int num = ThreadLocalRandom.current().nextInt(1,220);
        return String.format("http://cd.ray-live.cn/imgs/headpic/pic_%s.jpg", num);
    }

}
