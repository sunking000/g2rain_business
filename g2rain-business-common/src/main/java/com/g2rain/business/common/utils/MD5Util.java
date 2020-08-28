package com.g2rain.business.common.utils;

import java.security.MessageDigest;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class MD5Util {
	
	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	public static String md5(Map<String, String> signData) {
		String inStr = JSONObject.toJSONString(signData);
		String result = md5(inStr);
		logger.info("signData:{}, signResult:{}", inStr, result);
		return result;
	}

	/*** 
     * MD5加码 生成32位md5码 
     */  
    public static String md5(String inStr){  
    	
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
        	logger.error(e.getMessage(), e);
            return null;  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++) {
        	byteArray[i] = (byte) charArray[i];  
        }
        
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = (md5Bytes[i]) & 0xff;  
            if (val < 16) {
            	hexValue.append("0");  
            }
            hexValue.append(Integer.toHexString(val));  
        }  
        
        return hexValue.toString();  
    }  
    
    public static void main(String[] args) {
		System.out.println(MD5Util.md5("011865"));
	}
}
