package com.grepp.moodlink.infra.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.username}")
    private String username;
    @Value("${spring.data.redis.password}")
    private String password;
    
    private final ApplicationContext applicationContext;
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setUsername(username);
        configuration.setPort(port);
        configuration.setHostName(host);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }
    
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        
        ClassLoader loader = getClass().getClassLoader();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules( SecurityJackson2Modules.getModules(loader));
        
        // 1. Java 8 Date/Time API 지원을 위한 모듈 등록 (필수 권장)
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 날짜를 타임스탬프가 아닌 문자열로 직렬화
        
        mapper.activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType("org.springframework.security.") // 기존 설정 (유지)
                .allowIfBaseType("com.grepp.spring.")    // 당신의 도메인 객체 패키지 (유지)
                .build(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

}
