package com.g2rain.business.core.vo;

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
	private String organAddress;
	// 真实姓名
	private String name;
	private boolean adminCompany;
	private String sessionType;
	private boolean adminUser;
	// aksk
	private String accessKey;
	private String secretAccessKey;

	public LoginToken() {
		super();
	}
}
