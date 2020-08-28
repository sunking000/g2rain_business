package com.g2rain.business.common.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HmacSHA256Util {

	private static Logger logger = LoggerFactory.getLogger(HmacSHA256Util.class);

	public static boolean verify(Map<String, String> signParam, String secretAccessKey, String sign) {
		String dataString = getDataString(signParam);
		String hash = encrypt(secretAccessKey, dataString);
		logger.info("signParam:{},signData:{},server sign:{};param sign:{}", JSONObject.toJSONString(signParam),
				dataString, hash,
				sign);
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
			String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
			return hash;
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public static void main(String[] args) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("ap", "ap");
		param.put("bp", "bp");
		param.put("Ap", "Ap");
		param.put("Bp", "Bp");

		// System.out.println(JSONObject.toJSONString(getDataString(param)));
		//
		// System.out.println("s:" + encrypt("m0tMYnX1", "hello world"));
		// System.out.println("m:" + encrypt("rtmC6wepncbYayf6", "hello world"));
		// System.out.println(
		// "l:" + encrypt("53a208ef9d4b44db9ca453a5bb6da08e", "hello world"));
		// System.out.println("test:" + encrypt("12a4a5ae1ff84f40aa746036ccd084f2",
		// "ORDER_IMGOR1092111"));
		// System.out.println("test:" + encrypt("ORDER_IMGOR1092111",
		// "12a4a5ae1ff84f40aa746036ccd084f2"));
		System.out.println("N0ZT1rmbrmHccdVU:" + encrypt("2mFOidnWmvp3QtAs",
				"0a20c1d4118e4e588edf1a428dda4dbaa5168df32a9b4254a940a7e4bb3ceb0d2018-12-24 13:18:21/core/order/181223-204642 机号:13668"));
	}
}
