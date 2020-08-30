package com.g2rain.business.gateway.adapter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.g2rain.business.gateway.adapter.domain.LoginToken;
import com.g2rain.business.gateway.exception.BaseResult;
import com.g2rain.business.gateway.exception.BussinessRuntimeException;
import com.g2rain.business.gateway.exception.ErrorCodeEnum;
import com.g2rain.business.gateway.rc.CustomizeHeaderKeyEnum;
import com.g2rain.business.gateway.utils.JsonObjectUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoreClient {

	@Value("${core.client.host}")
	private String host;

	@Autowired
	private RestTemplate restTemplate;

	public String getHoldStoreIds(String organId) {
		String urlString = host + "/organ_membership/hold_store?organId=" + organId;

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(CustomizeHeaderKeyEnum.BACK_EDN.getUpper(), "true");
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlString, HttpMethod.GET, requestEntity,
				String.class);
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			log.error("调用接口错误，url:{}, organId:{}, header:{}, response:{}", urlString, organId,
					JsonObjectUtil.toJson(headers), responseEntity.getBody());
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}

		String body = responseEntity.getBody();
		JsonObject bodyJsonObject = JsonObjectUtil.parseObject(body);
		if (bodyJsonObject.get("status").getAsInt() != BaseResult.SUCCESS.getStatus()) {
			return null;
		}

		if (bodyJsonObject.has("resultData")) {
			JsonArray jsonArray = bodyJsonObject.getAsJsonArray("resultData");
			StringBuffer holdStoreIdsStringBuffer = new StringBuffer();
			if (jsonArray.size() > 0) {
				for (int i = 0; i < jsonArray.size(); i++) {
					holdStoreIdsStringBuffer.append(jsonArray.get(i).getAsString()).append(",");
				}
				holdStoreIdsStringBuffer.deleteCharAt(holdStoreIdsStringBuffer.length() - 1);
			}
			return holdStoreIdsStringBuffer.toString();
		}

		return "";
	}

	public String getOrganName(String organId) {
		String urlString = host + "/organ/" + organId;

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(CustomizeHeaderKeyEnum.BACK_EDN.getUpper(), "true");
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlString, HttpMethod.GET, requestEntity,
				String.class);
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			log.error("调用接口错误，url:{}, organId:{}, header:{}, response:{}", urlString,
					organId, JsonObjectUtil.toJson(headers),
					responseEntity.getBody());
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}

		String body = responseEntity.getBody();
		JsonObject bodyJsonObject = JsonObjectUtil.parseObject(body);
		if (bodyJsonObject.get("status").getAsInt() != BaseResult.SUCCESS.getStatus()) {
			return null;
		}

		if (bodyJsonObject.has("resultData")) {
			JsonObject dataJsonObject = bodyJsonObject.getAsJsonObject("resultData");
			return dataJsonObject.has("name") ? dataJsonObject.get("name").getAsString()
					: null;
		}


		return null;
	}

	public LoginToken getLoginToken(String accessKey) {
		String urlString = host + "/user_auth/login_token?accessKey={accessKey}";
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("accessKey", accessKey);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlString, String.class, uriVariables);
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new BussinessRuntimeException(ErrorCodeEnum.TOKEN_EXP_ERROR);
		}

		String body = responseEntity.getBody();
		JsonObject bodyJsonObject = JsonObjectUtil.parseObject(body);
		if (bodyJsonObject.get("status").getAsInt() != BaseResult.SUCCESS.getStatus()) {
			log.error("response body : {}", body);
			throw new BussinessRuntimeException(ErrorCodeEnum.TOKEN_EXP_ERROR);
		}

		JsonElement jsonElement = bodyJsonObject.getAsJsonObject("resultData");
		Gson gson = new Gson();
		LoginToken loginToken =  gson.fromJson(jsonElement, LoginToken.class);

		return loginToken;
	}
}
