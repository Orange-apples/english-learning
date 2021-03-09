package com.cxylm.springboot.util;

import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.enums.SMSType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisCacheUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redisson;

    private final static int THREE = 3;
    private final static String TRUE = "true";

    @Autowired
    public RedisCacheUtil(RedisTemplate<String, Object> redisTemplate, RedissonClient redisson) {
        this.redisTemplate = redisTemplate;
        this.redisson = redisson;
    }

    /**
     * 设置缓存（无限期）
     *
     * @param cacheName 缓存名
     * @param key       缓存键值
     * @param value     缓存值
     */
    public void set(String cacheName, String key, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        set(cacheName + ":" + key, value);
    }

    public void set(String cacheKey, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(cacheKey, value);
    }

    /**
     * 设置缓存（指定过期时间）
     *
     * @param cacheName     缓存名
     * @param key           缓存键值
     * @param value         缓存值
     * @param expireSeconds 过期时间（秒）
     */
    public void set(String cacheName, String key, Object value, long expireSeconds) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(cacheName + ":" + key, value, expireSeconds, TimeUnit.SECONDS);
    }

    public void set(String cacheKey, Object value, long expireSeconds) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(cacheKey, value, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存值
     *
     * @param cacheName 缓存名
     * @param key       缓存键值
     * @return 缓存值
     */
    public Object get(String cacheName, String key) {
        return get(cacheName + ":" + key);
    }

    public Object get(String cacheKey) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(cacheKey);
    }

    /**
     * 删除缓存值
     *
     * @param cacheName 缓存名
     * @param key 缓存键
     */
    public void delete(String cacheName, String key) {
        redisTemplate.delete(cacheName + ":" + key);
    }

    public void delete(String fullKey) {
        redisTemplate.delete(fullKey);
    }

    public void setToHashMap(String cacheName, String key, Object value) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.put(cacheName, key, value);
    }

    public Object getValueFromHashMap(String cacheName, String key) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.get(cacheName, key);
    }

    public List<Object> multiGetFromHashMap(String cacheName, Collection<String> keys) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.multiGet(cacheName, keys);
    }

    public Long deleteFromHashMap(String cacheName, String... keys) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.delete(cacheName, (Object[]) keys);
    }

    public Map<String, Object> getHashMap(String cacheName) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.entries(cacheName);
    }

    public void setHashMap(String cacheName, Map<String, ?> map) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.putAll(cacheName, map);
    }

    public Long increase(String cacheName, String key, long increasement) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.increment(cacheName + ":" + key, increasement);
    }

    /**
     * 为哈希表中指定的值加上增量
     * @param cacheKey 缓存键名
     * @param mapKey 哈希表键名
     * @param increasement 增量
     * @return 加上增量后现值
     */
    public Long increaseMapValue(String cacheKey, String mapKey, long increasement) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.increment(cacheKey, mapKey, increasement);
    }

    public void expire(String cacheName, String key, Long seconds) {
        redisTemplate.expire(cacheName + ":" + key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取过期时间（单位：秒）
     *
     * @param cacheName 缓存名
     * @param key       缓存key键
     * @return 过期时间 (-1 表示永不过期， 0 为已过期， 大于0表示尚未过期)
     */
    public long getTTLSeconds(String cacheName, String key) {
        Long ttl = redisTemplate.getExpire(cacheName + ":" + key, TimeUnit.SECONDS);
        return ttl == null ? 0 : ttl;
    }

    /**
     * 添加至无序集合
     * @param cacheKey 缓存key
     * @param values 加入集合的元素
     * @return 集合元素个数
     */
    public Long addToSet(String cacheKey, Object... values) {
        SetOperations<String, Object> op = redisTemplate.opsForSet();
        return op.add(cacheKey, values);
    }

    public Long getSetSize(String cacheKey) {
        SetOperations<String, Object> op = redisTemplate.opsForSet();
        return op.size(cacheKey);
    }

    /**
     * 专用方法
     * 校验短信是否验证成功--发送
     * @param phone
     * @param type
     */
    public void checkSmsPost(String phone, SMSType type) {
        RMap<Object, Object> map = redisson.getMap(CacheName.CHECK_SMS_POST);
        map.put(phone,type);
        map.expire(THREE,TimeUnit.MINUTES);
    }

    /**
     * 专用方法
     * 校验短信是否验证成功--校验
     * @param phone
     * @param type
     */
    public boolean checkSmsPostResult(String phone, SMSType type) {
        RMap<Object, Object> map = redisson.getMap(CacheName.CHECK_SMS_POST);
        if(!type.equals(map.get(phone))){
            return false;
        }
        map.remove(phone);
        return true;
    }

    /**
     * 缓存查询模板
     *
     * @param cacheSelector    查询缓存的方法
     * @param databaseSelector 数据库查询方法
     * @return T
     */
    public <T> T selectCacheByTemplate(CacheSelector<T> cacheSelector, Supplier<T> databaseSelector) {
        try {
            // 先查 Redis缓存
            T t = cacheSelector.select();
            if (t == null) {
                // 没有记录再查询数据库
                return databaseSelector.get();
            } else {
                return t;
            }
        } catch (Exception e) {
            // 缓存查询出错，则去数据库查询
            log.error("CacheSelector error：", e);
            return databaseSelector.get();
        }
    }
}
