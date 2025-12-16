package com.javasm.qingqing;

import com.javasm.qingqing.game.entity.Game;
import com.javasm.qingqing.game.service.GameService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.util.List;

@SpringBootTest
public class SpringMailTest {
    //SpringMail的客户端对象
    @Resource
    JavaMailSender javaMailSender;
    //获取配置文件中，关于邮件的配置
    @Resource
    MailProperties mailProperties;
    //收件人信息
    private final static String to = "chengdujavasm@126.com";

    //发送纯文本
    @Test
    void f1() throws Exception {
        //邮件客户端对象中，获取消息对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //消息辅助对象
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        //发件人
        String from = mailProperties.getUsername();
        helper.setFrom(from);
        //收件人
        helper.setTo(to);
        //标题
        helper.setSubject("今日新闻-----红军城又失守了");
        //内容
        helper.setText("忽如一夜冻风来");
        //发送
        javaMailSender.send(mimeMessage);

    }

    @Resource
    GameService gameService;

    @Test
    void f2() throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(to);
        helper.setSubject("测试HTML的案例");
        //拼接HTML代码
        List<Game> list = gameService.list();
        StringBuffer html = new StringBuffer();
        html.append("<h1>游戏列表</h1>");
        //循环table
        html.append("<table style='border:1px solid #000;text-align:center;'>");
        html.append("<tr><td>ID</td><td>游戏名</td><td>图标</td></tr>");
        list.forEach(game ->{
            html.append("<tr>");
            html.append("<td>");
            html.append(game.getId());
            html.append("</td>");
            html.append("<td>");
            html.append(game.getName());
            html.append("</td>");
            html.append("<td>");
            html.append("<img src='");
            html.append(game.getIcon());
            html.append("' style='width:50px;height:50px'>");
            html.append("</td>");
            html.append("</tr>");
        });
        html.append("</table>");
        html.append("<h2 style='color:red;'>游戏列表结束</h2>");
        //一张图片
        html.append("<img src='http://cd.ray-live.cn/imgs/headpic/pic_0.jpg'>");
        //设置邮件内容
        helper.setText(html.toString(),true);
        javaMailSender.send(mimeMessage);
    }

    @Test
    void f3() throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //multipart true 表示支持上传文件
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(to);
        helper.setSubject("本地图片上传测试");
        //内容
        StringBuilder html = new StringBuilder();
        html.append("<img src='cid:ei'>");
        helper.setText(html.toString(),true);
        //本地找一张图片
        File file = new File("D://save/pic_140.jpg");
        //文件上传
        helper.addInline("ei",file);
        //发送
        javaMailSender.send(mimeMessage);

    }

    @Test
    void f4() throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(to);
        helper.setSubject("测试附件");
        String html = "<img src='cid:ei'>";
        helper.setText(html,true);
        File img = new File("D://save/pic_140.jpg");
        helper.addInline("ei",img);
        helper.addAttachment("abc.png",img);
        //附件
        File fu1 = new File("D://save/t1.docx");
        helper.addAttachment("test.docx",fu1);
        //发送
        javaMailSender.send(mimeMessage);
    }

}
