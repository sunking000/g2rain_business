package com.g2rain.business.file.store.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.PolicyConditions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AliyunOssClient {
	@Value("${aliyun.oss.client.endpoint}")
	private String endpoint;
	@Value("${aliyun.oss.client.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.oss.client.accessKeySecret}")
	private String accessKeySecret;
	@Value("${aliyun.oss.client.bucketName}")
	private String bucketName;
	@Value("${aliyun.oss.client.dir}")
	private String dir;
	@Value("${server.context.path}")
	private String uploadCallbackDomain;
	private OSS client;
	private static final long expireTime = 300;
	private final static String callbackUrl = "/direct/callback/aliyun";

	@PostConstruct
	public void init() {
		// 创建OSSClient实例。
		client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	}

	public String generatePresignedUrl4Get(String fileId) {
		// 设置URL过期时间为1小时。
		Date expiration = new Date(new Date().getTime() + expireTime * 1000);
		// 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
		URL url = client.generatePresignedUrl(bucketName, fileId, expiration);
		return url.toString();
	}

	public AliyunOssSignatureResult generatePresignedUrl4Upload(String fileId) {
		AliyunOssSignatureResult result = new AliyunOssSignatureResult();
		long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
		Date expiration = new Date(expireEndTime);
		PolicyConditions policyConds = new PolicyConditions();
		policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);

		String postPolicy = client.generatePostPolicy(expiration, policyConds);
		byte[] binaryData = null;
		try {
			binaryData = postPolicy.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String encodedPolicy = BinaryUtil.toBase64String(binaryData);
		String postSignature = client.calculatePostSignature(postPolicy);

		Map<String, String> respMap = new LinkedHashMap<String, String>();
		String host = "https://" + bucketName + "." + endpoint;
		result.setAccessId(accessKeyId);
		result.setEncodedPolicy(encodedPolicy);
		result.setPostSignature(postSignature);
		result.setHost(host);
		result.setExpire(String.valueOf(expireEndTime / 1000));

		JSONObject jasonCallback = new JSONObject();
		jasonCallback.put("callbackUrl", uploadCallbackDomain + callbackUrl);
		jasonCallback.put("callbackBody", "fileId=" + fileId + "&bucket=" + bucketName);
		jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
		log.debug("callback json:{}", jasonCallback.toJSONString());

		String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
		respMap.put("callback", base64CallbackBody);
		result.setCallback(base64CallbackBody);
		return result;
	}

	@PreDestroy
	public void destroy() {
		client.shutdown();
		client = null;
	}
}
