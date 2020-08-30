package com.g2rain.business.gateway.adapter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginToken {
	private String userId;
	private String deviceId;
	private String deviceName;
	private String organType;
	private String organId;
	private String organName;
	// 真实姓名
	private String name;
	private String sessionType;
	private boolean adminUser;
	private boolean adminCompany;
	// aksk
	private String accessKey;
	private String secretAccessKey;

	public LoginToken() {
		super();
	}
}
