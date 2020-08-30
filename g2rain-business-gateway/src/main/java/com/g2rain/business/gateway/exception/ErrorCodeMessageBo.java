package com.g2rain.business.gateway.exception;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.g2rain.business.gateway.utils.JsonObjectUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ErrorCodeMessageBo {

	private Map<String, String> errorCodeMessages;
	private Map<String, String> subErrorCodeMessages;

	@Value("${error.code.message}")
	public void setErrorCodeMessage(String errorCodeMessage) {
		if (StringUtils.isNotBlank(errorCodeMessage)) {
			try {
				errorCodeMessage = new String(errorCodeMessage.getBytes("ISO8859-1"));
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			this.errorCodeMessages = new HashMap<String, String>();
			JsonObject errorCodeMessageObject = JsonObjectUtil.parseObject(errorCodeMessage);
			// JSONObject jsonObject = JSONObject.parseObject(errorCodeMessage);

			for (Map.Entry<String, JsonElement> entry : errorCodeMessageObject.entrySet()) {
				this.errorCodeMessages.put(entry.getKey(), entry.getValue().getAsString());
			}
		}
	}

	@Value("${sub.error.code.message}")
	public void setSubErrorCodeMessage(String subErrorCodeMessage) {
		if (StringUtils.isNotBlank(subErrorCodeMessage)) {
			try {
				subErrorCodeMessage = new String(subErrorCodeMessage.getBytes("ISO8859-1"));
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			this.subErrorCodeMessages = new HashMap<String, String>();
			JsonObject errorCodeMessageObject = JsonObjectUtil.parseObject(subErrorCodeMessage);
			for (Map.Entry<String, JsonElement> entry : errorCodeMessageObject.entrySet()) {
				this.subErrorCodeMessages.put(entry.getKey(), entry.getValue().getAsString());
			}
		}
	}

	public String getErrorMessage(String errorCode) {
		if (MapUtils.isNotEmpty(errorCodeMessages)) {
			return errorCodeMessages.containsKey(errorCode) ? errorCodeMessages.get(errorCode) : errorCode;
		}

		return errorCode;
	}

	public String getSubErrorMessage(String subErrorCode, String defaultMessage) {
		if (MapUtils.isNotEmpty(subErrorCodeMessages)) {
			return subErrorCodeMessages.containsKey(subErrorCode) ? subErrorCodeMessages.get(subErrorCode)
					: StringUtils.isNotBlank(defaultMessage) ? defaultMessage : subErrorCode;
		}

		return StringUtils.isNotBlank(defaultMessage) ? defaultMessage : subErrorCode;
	}
}
