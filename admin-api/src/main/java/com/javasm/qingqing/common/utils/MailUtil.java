package com.javasm.qingqing.common.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;


@Component
public class MailUtil {

    @Resource
    JavaMailSender javaMailSender;
    @Resource
    MailProperties mailProperties;

    public void sendMail(String email , String title, String count, File... files){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //配置发件人
            helper.setFrom(mailProperties.getUsername());
            //配置收件人
            helper.setTo(email);
            //标题
            helper.setSubject(title);
            //内容
            helper.setText(count,true);
            //判断附件
            if (files != null){
                for (File file : files){
                    helper.addAttachment(file.getName(),file);
                }
            }
            //发送
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "jpg","jpeg","png","gif","bmp","wbp"
    );

    //用来判断，file是不是图片类型
    public boolean isImage(File file){
        //文件对象null || 文件不存在||文件不是文件是文件夹
        if (file == null || !file.exists() || !file.isFile()){
            return false;
        }
        //扩展名
        String fileName = file.getName();
        //后缀
        int indexOf = fileName.lastIndexOf(".");
        if (indexOf == -1) return false;
        //获取扩展名
        String extension = fileName.substring(indexOf + 1).toLowerCase();
        if (!IMAGE_EXTENSIONS.contains(extension)){
            return false;
        }
        //校验实际的内容是不是
        try {
            BufferedImage image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }

}
