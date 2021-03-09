package com.cxylm.springboot.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class RedissonConfig {

    private final RedisProperties redisProperties;

    public RedissonConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        String prefix = redisProperties.isSsl() ? "rediss://" : "redis://";

        // 目前暂时用单机模式，将来若部署redis集群请使用useClusterServers
        config.useSingleServer()
                .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setConnectTimeout(Math.toIntExact(redisProperties.getTimeout().toMillis()))
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword());
        config.setCodec(JsonJacksonCodec.INSTANCE);
        return Redisson.create(config);
    }
}
