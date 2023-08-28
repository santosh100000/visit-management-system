package io.bootify.visit_managment_system.config;

import io.bootify.visit_managment_system.model.VisitDTO;
import io.bootify.visit_managment_system.model.VisitListRedisDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
//        lettuceConnectionFactory.setHostName("localHost");
//        lettuceConnectionFactory.setPort(6379);
//        return lettuceConnectionFactory;
//    }

    @Bean
    public RedisTemplate<String, VisitListRedisDTO> productRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,  VisitListRedisDTO> redisTemplate = new  RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer< VisitListRedisDTO>(VisitListRedisDTO.class));
        return redisTemplate;
    }
}
