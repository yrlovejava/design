package com.book.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCommonProcessor {

    @Autowired
    private RedisTemplate redisTemplate;

    public Object get(String key){
        if(key == null){
            throw new UnsupportedOperationException("Redis key could not be null");
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,Object value,long timeout){
        if(timeout > 0){
            redisTemplate.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
        }else {
            set(key,value);
        }
    }

    //根据key删除Redis缓存数据
    public void remove(String key){
        redisTemplate.delete(key);
    }
}
