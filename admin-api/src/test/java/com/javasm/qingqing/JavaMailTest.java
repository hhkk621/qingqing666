package com.javasm.qingqing;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

@SpringBootTest
public class JavaMailTest {

    //这个案例是纯Java实现的，比较繁琐，看一看就可以了
    //通过这个案例，来了解，发送邮件底层都需要哪些角色
    @Test
    void f1() throws Exception {
        //定义账号信息
        //发件人
        String account = "17335592100@163.com";
        //收件人
        String to = "chengdujavasm@126.com";
        //发件人密码
        String password = "CLQ5XnDsUs3nxbRS";
        //SMTP信息
        Properties properties = new Properties();
        //连接协议
        properties.put("mail.ransport.protocol","smtp");
        //邮箱域名
        properties.put("mail.smtp.host","smtp.163.com");
        //端口号  默认是25
        properties.put("mail.smtp.port",465);
        //服务端认证
        properties.put("mail.smtp.auth","true");
        //安全连接
        properties.put("mail.smtp.ssl.enable","true");
        //初始化，会话对象
        Session session = Session.getDefaultInstance(properties);
        //发消息对象
        Message msg = new MimeMessage(session);
        //配置 发送人信息
        msg.setFrom(new InternetAddress(account));
        //配置 收件人信息
        msg.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
        //配置  抄送人信息
        msg.setRecipient(Message.RecipientType.CC,new InternetAddress(account));
        //邮件标题
        msg.setSubject("今日天气");
        //邮件内容
        msg.setText("晴天，0~11°");
        //邮差对象   这封信，是由邮差交给收件人
        Transport transport = session.getTransport();
        //配置 邮箱的用户名和密码
        transport.connect(account,password);
        //邮差 发送邮件
        transport.sendMessage(msg,msg.getAllRecipients());
    }
}
