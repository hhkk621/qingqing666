package com.javasm.qingqing.common.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    //创建RedisTemplate的Bean，Spring容器会自动管理它
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        //1 创建RedisTemplate对象，指定key是String类型的，value为任意对象
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        //2 设置连接工厂，让Template知道如何连接Redis
        template.setConnectionFactory(factory);
        //3 配置Jackson的ObjectMapper（JSON 处理的核心工具 jackson）
        ObjectMapper om = new ObjectMapper();
        // 4 设置所有字段可见，包括私有字段，这样才能序列化对象的所有属性
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //5 启用自动类型识别（反序列化时，能正确还原对象类型）
        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,//类型验证器
                ObjectMapper.DefaultTyping.NON_FINAL//对非final类记录信息
        );
        //6 创建json序列化器，用于将java对象 → json 互相转换
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(om,Object.class);
        //7 创建字符串序列化器，用于处理key的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //8 设置普通key 的序列化方式 字符串格式
        template.setKeySerializer(stringRedisSerializer);
        // 9 设置Hash类型的key的序列化方式，字符串格式--这里设置的是 field，后续所有的 field类型必须是String
        template.setHashKeySerializer(stringRedisSerializer);
        //10 设置普通value的序列化方式，json
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //11 设置hash类型value的序列化方式  json
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        //12 初始化配置
        template.afterPropertiesSet();
        //返回对象
        return template;
    }
}
