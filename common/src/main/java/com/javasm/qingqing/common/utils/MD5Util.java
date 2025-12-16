package com.javasm.qingqing.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 对明文字符串进行 MD5 加密
     *
     * @param input 明文字符串
     * @return 加密后的 MD5 字符串
     */
    public static String encrypt(String input) {
        try {
            // 获取 MD5 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为 UTF-8 编码的字节数组
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 计算 MD5 哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b & 0xff));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // 若 MD5 算法不可用（极小概率），抛出运行时异常
            throw new RuntimeException("MD5 算法不可用", e);
        }
    }
}
