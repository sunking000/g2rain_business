package com.g2rain.business.core.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginParam {
	private String username;
	private String password;
	private String accessKey;
	private String secretAccessKey;

	public UserLoginParam() {
		super();
	}

}
