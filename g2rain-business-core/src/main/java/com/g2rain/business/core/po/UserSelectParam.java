package com.g2rain.business.core.po;

import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSelectParam extends BaseSelectParam {
	private String username;
	private String organId;
	private String email;
	private String mobile;
	private String sex;
	// 身份证号
	private String idNo;
	private String password;
	private String accessKey;
	private String secretAccessKey;
	private String userId;
	private Boolean adminFlag;

	public UserSelectParam() {
		super();
	}
}