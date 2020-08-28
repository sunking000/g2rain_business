package com.github.sunking000.g2rain.business.gateway.auth;

import org.springframework.stereotype.Service;

import com.github.sunking000.g2rain.business.gateway.adapter.domain.LoginToken;
import com.github.sunking000.g2rain.business.gateway.rc.Context;
import com.github.sunking000.g2rain.business.gateway.utils.HmacSHA256Util;
import com.github.sunking000.g2rain.business.gateway.utils.JsonObjectUtil;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthBo {

	public boolean verifySign(Context context) {
		LoginToken loginToken = context.getLoginToken();
		String secretAccessKey = loginToken.getSecretAccessKey();
		JsonObject signParam = JsonObjectUtil.parseObject(context.getParameterString());
		// JSONObject signParam = JSONObject.parseObject(context.getParameterString());
		signParam.addProperty("path", context.getPath());
		signParam.addProperty("X-REQUEST-ID", context.getRequestId());
		signParam.addProperty("X-REQUEST-TIME", context.getRequestTime());
		signParam.addProperty("X-ACCESS-KEY", context.getAccessKey());
		if (!HmacSHA256Util.verify(HmacSHA256Util.getDataString(signParam), secretAccessKey, context.getSign())) {
			log.error("检验错误:signParam:{},publicKey:{},sign:{}",
					HmacSHA256Util.getDataString(signParam), secretAccessKey,
					context.getSign());
			return false;
		} else {
			return true;
		}
	}

	public String getDataString(Context context) {
		JsonObject signParam = JsonObjectUtil.parseObject(context.getParameterString());
		// JSONObject signParam = JSONObject.parseObject(context.getParameterString());
		signParam.addProperty("path", context.getPath());
		signParam.addProperty("X-REQUEST-ID", context.getRequestId());
		signParam.addProperty("X-REQUEST-TIME", context.getRequestTime());
		signParam.addProperty("X-ACCESS-KEY", context.getAccessKey());
		return HmacSHA256Util.getDataString(signParam);
	}

}
