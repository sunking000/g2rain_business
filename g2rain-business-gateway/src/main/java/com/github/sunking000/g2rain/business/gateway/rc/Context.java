package com.github.sunking000.g2rain.business.gateway.rc;

import java.util.Locale;

import org.slf4j.MDC;

import com.github.sunking000.g2rain.business.gateway.adapter.domain.LoginToken;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Context {
	private String requestTime;
	private String requestId;
	private String organId;
	private String organType;
	private String userId;
	private String memberId;
	private String deviceId;
	private boolean adminUser;
	private boolean adminCompany;
	private boolean debugFlag;
	private String sessionType;
	private Locale locale = Locale.SIMPLIFIED_CHINESE;
	private String apiContextPath;
	private String apiVersion;
	private String apiPath;
	private String method;
	private String accessKey;
	private String sign;
	private String debugKey;
	// 请求入参
	private String parameterString;
	// 进行时缓存
	private LoginToken loginToken;

	public Context() {
		super();
	}

	public void setDebugFlag(boolean debugFlag) {
		this.debugFlag = debugFlag;
		if (this.debugFlag) {
			MDC.put("DEBUG_PRINT_LEVEL", "TRUE");
		}
	}

	public void setLoginToken(LoginToken loginToken) {
		this.loginToken = loginToken;
		this.userId = loginToken.getUserId();
		this.deviceId = loginToken.getDeviceId();
		this.sessionType = loginToken.getSessionType();
		this.adminCompany = loginToken.isAdminCompany();
		this.organId = loginToken.getOrganId();
		this.organType = loginToken.getOrganType();
		this.adminUser = loginToken.isAdminUser();
	}

	public void setOriginalRequestPath(String originalRequestPath) {
		String[] paths = originalRequestPath.split("/");
		String apiContext = paths[1];
		String apiVersion = paths[2];
		StringBuilder apiStringBuilder = new StringBuilder();
		for (int i = 3; i < paths.length; i++)
			apiStringBuilder = apiStringBuilder.append("/").append(paths[i]);
		String apiPath = apiStringBuilder.toString();
		setApiContextPath(apiContext);
		setApiVersion(apiVersion);
		setApiPath(apiPath);
	}

	public String getPath() {
		return "/" + apiContextPath + apiPath;
	}
}
