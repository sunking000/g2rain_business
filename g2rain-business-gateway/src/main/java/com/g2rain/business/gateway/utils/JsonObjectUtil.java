package com.g2rain.business.gateway.utils;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonObjectUtil {

	/**
	 * 对象转换为json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}

		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		return StringUtils.isBlank(jsonString) ? jsonString : jsonString.replaceAll("\\\"", "\"");
	}

	/**
	 * json字符串解析为JsonObject对象
	 * 
	 * @param json
	 * @return
	 */
	public static JsonObject parseObject(String json) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(json);
		JsonObject jsonObject = null;
		if (jsonElement.isJsonObject()) {
			jsonObject = parser.parse(json).getAsJsonObject();
		}

		return jsonObject;
	}

	public static void main(String[] args) {
		System.out.println(
				"{\\\"name\\\":\\\"餐费\\\",\\\"num\\\":\\\"1\\\",\\\"price\\\":\\\"10.5\\\",\\\"productId\\\":\\\"307040100000000000001\\\"}"
						.replace("\\\"", "\""));
	}
}
