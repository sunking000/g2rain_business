package com.g2rain.business.core.bo;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.core.vo.LoginToken;

@Service
public class LoginTokenStorageBo {
	
	private final static String PREFIX = "USER_TOKEN_";
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	public long getDefaultExpireTime() {
		return 259200;
	}

	public void setLoginToken(LoginToken token) {
		String key = getRedisKey(token.getAccessKey());
		redisTemplate.opsForValue().set(key, JSONObject.toJSONString(token),
				getDefaultExpireTime(), TimeUnit.SECONDS);
	}

	public LoginToken getLoginToken(String accessKey) {
		
		String key = getRedisKey(accessKey);
		String loginTokenString = redisTemplate.opsForValue().get(key);
		if (loginTokenString != null) {
			redisTemplate.expire(key, getDefaultExpireTime(), TimeUnit.SECONDS);
			LoginToken loginToken = JSONObject.parseObject(loginTokenString, LoginToken.class);
			return loginToken;
		}

		return null;
	}

	public void deleteSession(String accessKey) {
		String sessionIdKey = getRedisKey(accessKey);
		redisTemplate.opsForValue().getOperations().delete(sessionIdKey);
	}
	

	public void expire(String accessKey) {
		String key = getRedisKey(accessKey);
		redisTemplate.expire(key, getDefaultExpireTime(),
				TimeUnit.SECONDS);		
	}

	public String getRedisKey(String accessKey) {
		String redisKey = PREFIX + accessKey;
		return redisKey;
	}
}
