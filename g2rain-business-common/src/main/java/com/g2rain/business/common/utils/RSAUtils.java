package com.g2rain.business.common.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAUtils {
private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);
	
	/**  
     * 加密算法RSA  
     */  
    public static final String KEY_ALGORITHM = "RSA";  
  
    /**  
     * 签名算法  
     */  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";  
  
    /**  
     * 获取公钥的key  
     */  
    public static final String PUBLIC_KEY = "RSAPublicKey";  
  
    /**  
     * 获取私钥的key  
     */  
    public static final String PRIVATE_KEY = "RSAPrivateKey";  

	
	
	/**
	 * 
	 * @Title getKeys
	 * @Description 获取公钥和私钥
	 * @return
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月24日 下午11:27:13
	 */
	public static Map<String, String> getKeys() {
		Map<String, String> keys = new HashMap<String, String>();
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			return null;
		}  
		
        keyPairGen.initialize(1024);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        keys.put(PUBLIC_KEY, Base64.encodeBase64String(publicKey.getEncoded()));
        keys.put(PRIVATE_KEY, Base64.encodeBase64String(privateKey.getEncoded()));
        
		return keys;
	}
	
	/**
	 * 
	 * @Title sign
	 * @Description 为data添加签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午12:12:01
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {  
        byte[] keyBytes = Base64.decodeBase64(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initSign(privateK);  
        signature.update(data);  
        return Base64.encodeBase64String(signature.sign());  
    } 
	
	/**
	 * 
	 * @Title verify
	 * @Description 校验data的sign是否正确
	 * @param data
	 * @param publicKey
	 * @param sign
	 * @return
	 * @throws Exception
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午12:12:27
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
        byte[] keyBytes = Base64.decodeBase64(publicKey);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicK = keyFactory.generatePublic(keySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(publicK);  
        signature.update(data);  
        return signature.verify(Base64.decodeBase64(sign));  
    }
	
	/**
	 * 
	 * @Title sign
	 * @Description 获取签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午12:34:52
	 */
	public static String sign(Map<String, String> data, String privateKey) throws Exception {
		if (data == null || data.isEmpty()) {
			return null;
		}

		String dataString = getDataString(data);
		String md5Value = MD5Util.md5(dataString);
		String signVaue = sign(Base64.decodeBase64(md5Value), privateKey);
		
		return signVaue;
	}
	
	/**
	 * 
	 * @Title verify
	 * @Description 验证data的签名sign是否为publicKey对应的私钥生成
	 * @param data
	 * @param publicKey
	 * @param sign
	 * @return
	 * @throws Exception
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午10:19:02
	 */
	public static boolean verify(Map<String, String> data, String publicKey, String sign) throws Exception {
		
		if (data == null || data.isEmpty()) {
			return false;
		}
		String dataString = getDataString(data);
		
		String md5Value = MD5Util.md5(dataString);
		
		return verify(Base64.decodeBase64(md5Value), publicKey, sign);
	}
	
	/**
	 * 
	 * @Title sign
	 * @Description 获取dataString使用privateKey的签名
	 * @param dataString
	 * @param privateKey
	 * @return
	 * @throws Exception
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午10:20:53
	 */
	public static String sign(String dataString, String privateKey) throws Exception {
		if (dataString == null) {
			return null;
		}
		
		String md5Value = MD5Util.md5(dataString);
		String signVaue = sign(Base64.decodeBase64(md5Value), privateKey);
		
		return signVaue;
	}
	
	/**
	 * 
	 * @Title verify
	 * @Description 验证dataString的签名sign是否为publicKey对应的私钥生成
	 * @param dataString
	 * @param publicKey
	 * @param sign
	 * @return
	 * @throws Exception
	 *
	 * @author sunhaojie 3113751575@qq.com
	 * @date 2016年8月25日 上午10:22:52
	 */
	public static boolean verify(String dataString, String publicKey, String sign) throws Exception {
		
		if (dataString == null) {
			return false;
		}
		
		String md5Value = MD5Util.md5(dataString);
		return verify(Base64.decodeBase64(md5Value), publicKey, sign);
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

		Map<String, String> sortMap = new TreeMap<String, String>(
				new Comparator<String>(){
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
	
	public static void main(String[] args) throws Exception {
//		Map<String, String> keys = RSAUtils.getKeys();
//		System.out.println(keys.get(PUBLIC_KEY));
//		System.out.println(keys.get(PRIVATE_KEY));
//		
//		Map<String, String> param = new HashMap<String, String>();
//		for(int i=0; i<10; i++) {
//			param.put("key" + i, "value" + i);
//		}
//		String sign = sign(param, keys.get(PRIVATE_KEY));
//		System.out.println(sign);
//		System.out.println(verify(param, keys.get(PUBLIC_KEY), sign));
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("appId", "test_20180402");
		param.put("path", "/core/user_auth/login");
		param.put("timestamp", "2018-04-02 12:12:12");
		param.put("mobile", "13800138000");
		param.put("password", "123456");
		param.put("companyOrganId", "9999");
		
		String sign = sign(
				param,
				"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMx9FkSv2+Qr4saZmbtaohI0jvisiK/mZW/w52CKw1W4VryVoJJ/o1kQymcrKvgByC/GxZxt6mKt5/wMbGTrM1W1s96urk6rNz7DKlXtmvfYbc+IenPGaESKeWAzoDHftFBjYIkL8NKN40l557r4R7Ss1Iop9Qxt9RYe41Fcmj2BAgMBAAECgYEApkzsOTwTxkJykTUFszqIJ+oag94842CqChmO8hKnvSTHVWMSTVIEVEC1eqW+sicw2Ln8p7U0MsBfs7mexYwKGCpECGaXrVgVmoK3lv4eNw4anq6zxmIsm7y3GinHNGuphXGNIfMN5rrWoKT/UUHd7Ch8dZI6azA2zabHtPLGg0kCQQD84mYTqFelDpEHEA77/T4AYdO4BUpMkTCzjgdaH0Miz6P8AnSUGUQ7t0qqQIEicWakogPtMNhSzFi0g6XDtY3nAkEAzwIMG7gpzfAamic8zQd++X9rZ7K0bDHAnP0po8TIB5e2KKAlM5bqbs+/d/T4EH9d+gPxXT7euMBn5F42INhcVwJBANXfmBs5/d4f4rJJd5LRDBYAprH073CwPrMtBLTIydvwJq1lWjlC4T1r+F7jLRP3Oqs0RwPckqf8IW2SBajurLMCQHubcFl47VVwAqJ7vX179WDLqfmgpsVYK/hR3vRQQ1TtTTMS9UPJ4LBMUrsxRpKrSxVIF26jZOCS2zZs3wtbxj8CQFf3HaORT0ylLB/7BTwmU8JAdixjWVEreY7+Gs0EJ4kwUsGwgPx60lDuVwn2tejgTrQptlEJMs8qG9v2SStuFuw=");
		System.out.println(sign);
		boolean verify = verify(
				param,
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMfRZEr9vkK+LGmZm7WqISNI74rIiv5mVv8OdgisNVuFa8laCSf6NZEMpnKyr4AcgvxsWcbepiref8DGxk6zNVtbPerq5Oqzc+wypV7Zr32G3PiHpzxmhEinlgM6Ax37RQY2CJC/DSjeNJeee6+Ee0rNSKKfUMbfUWHuNRXJo9gQIDAQAB",
				sign);
		System.out.println(verify);
	}
}
