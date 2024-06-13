package com.project.travel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @version 1.0
 * @description: redis配置类
 * @date 2023/8/11 9:02
 */
@Configuration
public class RedisConfig {

    /**
     * 注入 RedisConnectionFactory
     */
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        setSerializer(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    /**
    * @description: 设置数据存入 redis 的序列化方式
    * @param: redisTemplate
    	factory
    * @return:
    * @date: 2023/9/14 11:05
    */
    private void setSerializer(RedisTemplate<String, Object> redisTemplate,
                               RedisConnectionFactory factory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
    }

}
