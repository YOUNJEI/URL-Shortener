package com.url.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValue(String key, String value, Integer timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.DAYS);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
