package com.cxylm.springboot.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存配置
 *
 * @author ZZ
 */
@EnableCaching
@Configuration
public class RedisConfig {
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setDefaultSerializer(redisSerializer);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @Primary
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        // 创建一个名称为"testMap"的缓存，过期时间ttl为24分钟，同时最长空闲时maxIdleTime为12分钟。
        //config.put("testMap", new CacheConfig(24*60*1000, 12*60*1000));
        final RedissonSpringCacheManager cacheManager = new RedissonSpringCacheManager(redissonClient, config);
        cacheManager.setAllowNullValues(false);
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
