package com.g2rain.business.core.bo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheBo<T> {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String,T> redisTemplate;

    //默认5.5分钟
	private int getDefaultExpireTime() {
		return 330;
	}

	public void setKey(String key,T t) {
		setKeyWithTime(key,t,getDefaultExpireTime());
	}

	public T getKey(String key){
		return redisTemplate.opsForValue().get(key);
	}

	public void setKeyWithTime(String key,T t,int timeout){
		redisTemplate.opsForValue().set(key, t);
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	public void removeKey(String key) {
		if (key == null) {
			return;
		}
		redisTemplate.delete(key);
	}
}
