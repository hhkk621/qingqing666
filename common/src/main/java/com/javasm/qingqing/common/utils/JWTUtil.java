package com.javasm.qingqing.common.utils;


import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTUtil {
    //生成的服务器秘钥
    private static final String keyStr = "Oon2+pDRDSxQ8uVt4zuDc1ogr9p0JjVJlNd2aVMqPWE=";

    private static final Integer expUidTime = 1000 * 60 * 60 * 24 * 15;
    private static final Integer expEmailTime = 1000 * 60 * 10;

    public static final String Server_Token = "javasm_token";


    public static String generateEmail(String email) {
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        return generateToken(map,expEmailTime);
    }
    //加密的方法，把uid加密成Token字符串
    public static String generateUid(String uid){
        Map<String,Object> map = new HashMap<>();
        map.put("uid",uid);
        return generateToken(map,expUidTime);
    }

    private static String generateToken(Map<String,Object> map,Integer expTime){
        //因为加密的字符串，要给一个过期时间
        //记录什么时候生成的Token
        Date time = new Date();
        //设置过期时间，这个字符串多久过期失效
        Date lastTime = new Date(time.getTime() + expTime);
        //生成一个Key
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyStr));
        return Jwts.builder()
                .setClaims(map) //加密的数据
                .setIssuedAt(time)  //标准签名，加密的时间
                .setExpiration(lastTime)    //过期时间
                .signWith(key)  //指定签名Key，Header中的秘钥
                .compact();
    }

    public static String parseEmail(String token){
        return parse("email",token);
    }

    public static String parseUid(String token){
        return parse("uid",token);
    }
    //解密
    private static String parse(String mapKey ,String token){
        //解密 获取和加密一样的key
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyStr));

        String string = null;
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Claims body = jws.getBody();
            //获取加密前的字符串
            string = body.get(mapKey, String.class);
        } catch (ExpiredJwtException e) {
            throw new JavasmException(ExceptionEnum.Token_Expired_Error);
        } catch (UnsupportedJwtException e) {
            throw new JavasmException(ExceptionEnum.Token_Unsupported_Error);
        } catch (SignatureException e) {
            throw new JavasmException(ExceptionEnum.Token_Signature_Error);
        } catch (Exception e) {
            throw new JavasmException(ExceptionEnum.SYSTEM_ERROR);
        }
        return string;
    }

    //生成服务端知道的秘钥，只有服务端能知道，不能泄露
    //不能随意更改，如果秘钥更改了，之前生成的所有Token都会失效
    //这个方法不会频繁执行，只会在生产秘钥的时候执行1次
    private static void  generateKey(){
        //指定算法
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        //生成加密的字符串
        String encode = Encoders.BASE64.encode(key.getEncoded());

        System.out.println(encode);
    }

    public static void main(String[] args) {
        //generateKey();
/*        String string = generateUid("100");
        System.out.println(string);*/
/*        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMDAiLCJpYXQiOjE3NjQ3Mjc0ODQsImV4cCI6MTc2NDcyNzQ5OX0.drNbsw8H61R2EUNJpgC0Yfe6E8M1TogHSpMwXxM1Gbg";
        String uid = parse(token);
        System.out.println(uid);*/
    }


}
