package com.github.sunking000.g2rain.business.gateway.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HmacSHA256Util {

	private static Logger logger = LoggerFactory.getLogger(HmacSHA256Util.class);

	public static boolean verify(Map<String, String> signParam, String secretAccessKey, String sign) {
		logger.debug("signParam:{}", JsonObjectUtil.toJson(signParam));
		String dataString = getDataString(signParam);
		logger.debug("signData:{}", dataString);
		String hash = encrypt(secretAccessKey, dataString);
		logger.debug("server sign:{};param sign:{}", hash, sign);
		return hash.equals(sign);
	}

	public static boolean verify(String signParam, String secretAccessKey, String sign) {
		logger.debug("signParam:{}", signParam);
		String hash = encrypt(secretAccessKey, signParam);
		logger.debug("server sign:{};param sign:{}", hash, sign);
		return hash.equals(sign);
	}

	/**
	 * 
	 * @Title getDataString
	 * @Description 返回data的value的值拼接字符串
	 * @param data
	 * @return
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午12:28:24
	 */
	private static String getDataString(Map<String, String> data) {
		if (data == null || data.isEmpty()) {
			return null;
		}

		Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
		});

		sortMap.putAll(data);

		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, String> entry : sortMap.entrySet()) {
			result.append(entry.getValue());
		}

		return result.toString();
	}

	public static String encrypt(String secretKey, Map<String, String> data) {
		String message = getDataString(data);
		return encrypt(secretKey, message);
	}

	public static String encrypt(String secretKey, String message) {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] doFinal = sha256_HMAC.doFinal(message.getBytes());
			String hash = Base64.encodeBase64String(doFinal);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * Hex字符串转byte
	 * 
	 * @param inHex
	 *            待转换的Hex字符串
	 * @return 转换后的byte
	 */
	public static byte hexToByte(String inHex) {
		return (byte) Integer.parseInt(inHex, 16);
	}

	/**
	 * hex字符串转byte数组
	 * 
	 * @param inHex
	 *            待转换的Hex字符串
	 * @return 转换后的byte数组结果
	 */
	public static byte[] hexToByteArray(String inHex) {
		int hexlen = inHex.length();
		byte[] result;
		if (hexlen % 2 == 1) {
			// 奇数
			hexlen++;
			result = new byte[(hexlen / 2)];
			inHex = "0" + inHex;
		} else {
			// 偶数
			result = new byte[(hexlen / 2)];
		}
		int j = 0;
		for (int i = 0; i < hexlen; i += 2) {
			result[j] = hexToByte(inHex.substring(i, i + 2));
			j++;
		}
		return result;
	}


	/**
	 * 字节数组转16进制
	 * 
	 * @param bytes
	 *            需要转换的byte数组
	 * @return 转换后的Hex字符串
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() < 2) {
				sb.append(0);
			}
			sb.append(hex);
		}
		return sb.toString();
	}


	public static void main(String[] args) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("X-POS-REQUEST-TIME", "2020-06-18 15:43:51");
		param.put("X-POS-ACCESS-KEY", "143d49658fd84632ba1915931bf7908d");
		param.put("X-POS-REQUEST-ID", "e588fdaf-f738-488a-b120-67303fef6ff2");
		param.put("appId", "SU_PIAO_TONG");
		param.put("pageNum", "2");
		param.put("path", "/core/order");
		System.out.println("before sign:" + getDataString(param));
		String sign = encrypt("UUz1y5jLdBbSiBFj", param);
		System.out.println("sign: " + sign);
	}

	public static String getDataString(JsonObject data) {
		logger.debug("original param:{}", data.toString());
		if (data == null || data.isJsonNull()) {
			return null;
		}

		Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
		});

		data.entrySet().forEach(item -> {
			JsonElement valueJsonElement = data.get(item.getKey());
			String value = null;
			if (valueJsonElement.isJsonArray()) {
				JsonElement jsonElement = sortParameter(valueJsonElement.getAsJsonArray());
				if (jsonElement != null && !jsonElement.isJsonNull()) {
					value = jsonElement.toString();
				}
			} else if (valueJsonElement.isJsonObject()) {
				JsonElement jsonElement = sortParameter(valueJsonElement.getAsJsonObject());
				if (jsonElement != null && !jsonElement.isJsonNull()) {
					value = jsonElement.toString();
				}
			} else {
				value = valueJsonElement.getAsString();
			}
			if (StringUtils.isNotBlank(value)) {
				sortMap.put(item.getKey(), value);
			}
		});
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, String> entry : sortMap.entrySet()) {
			result.append(entry.getValue());
		}

		return result.toString();
	}

	private static JsonElement sortParameter(JsonObject data) {
		if (data == null || data.isJsonNull()) {
			return null;
		}


		Map<String, JsonElement> sortMap = new TreeMap<String, JsonElement>(new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
		});

		data.entrySet().forEach(item -> {
			JsonElement valueJsonElement = data.get(item.getKey());
			JsonElement value = null;
			if (valueJsonElement.isJsonArray()) {
				value = sortParameter(valueJsonElement.getAsJsonArray());
			} else if (valueJsonElement.isJsonObject()) {
				value = sortParameter(valueJsonElement.getAsJsonObject());
			} else {
				value = valueJsonElement;
			}
			if (!valueJsonElement.isJsonNull()) {
				sortMap.put(item.getKey(), value);
			}
		});

		if (MapUtils.isNotEmpty(sortMap)) {
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(sortMap);
			return jsonElement;
		}

		return data;
	}

	private static JsonElement sortParameter(JsonArray jsonArray) {
		if (jsonArray == null || jsonArray.isJsonNull()) {
			return null;
		}

		int arraySize = jsonArray.size();
		if (arraySize > 0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement value = null;
				JsonElement valueJsonElement = jsonArray.get(i);
				if (valueJsonElement.isJsonArray()) {
					value = sortParameter(valueJsonElement.getAsJsonArray());
				} else if (valueJsonElement.isJsonObject()) {
					value = sortParameter(valueJsonElement.getAsJsonObject());
				} else {
					value = valueJsonElement;
				}
				if (!valueJsonElement.isJsonNull()) {
					jsonArray.set(i, value);
				}
			}
		}
		

		return jsonArray;
	}
}
