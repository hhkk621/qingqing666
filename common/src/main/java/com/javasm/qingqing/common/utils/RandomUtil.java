package com.javasm.qingqing.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


public class RandomUtil {
    //获取指定长度的 随机数字 6666  0123
    public static String getCode(int length) {
        if (length < 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<length;i++){
            int num = ThreadLocalRandom.current().nextInt(0,10);
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }
    private RandomUtil() {
    }

    private final static String pool =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //传入数字,返回对应长度的随机字符串
    public static String getString(int length){
        if (length <= 0){
            throw new IllegalArgumentException("参数异常,不能小于等于0");
        }
        StringBuilder result = new StringBuilder();
        for (int i=0;i<length;i++){
            int index = ThreadLocalRandom.current().nextInt(pool.length());
            char c = pool.charAt(index);
            result.append(c);
        }
        return result.toString();
    }

    //生成随机的文件名
    public static String getFileName(String filename){
        //abc.jpg
        int lastindex = filename.lastIndexOf(".");
        if (lastindex == -1){
            throw new IllegalArgumentException("文件名异常,没有文件类型");
        }
        //文件的类型
        String filetype = filename.substring(lastindex + 1);
        //文件名上加上时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = simpleDateFormat.format(new Date());
        return getString(5)+time+"."+filetype;
    }

}
