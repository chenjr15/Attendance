package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService implements ICacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setValue(String key, String value, long expireSec) {
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, expireSec, TimeUnit.SECONDS);

    }

    @Override
    public void setValue(String key, String value) {
        this.setValue(key, value, 0);
    }

    @Override
    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);

    }

    @Override
    public void setHashValue(String setName, String key, String value) {


    }

    @Override
    public void setHashValue(String setName, String key, String value, long expireSec) {
        stringRedisTemplate.opsForHash().put(setName, key, value);
        stringRedisTemplate.expire(key, expireSec, TimeUnit.SECONDS);

    }

    @Override
    public String getHashValue(String setName, String key) {
        return (String) stringRedisTemplate.opsForHash().get(setName, key);

    }
}
