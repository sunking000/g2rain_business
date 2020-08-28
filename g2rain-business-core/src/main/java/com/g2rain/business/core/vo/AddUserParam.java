package com.g2rain.business.core.vo;

import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.core.po.UserPo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserParam {
	private String userId;
	private String organId;
	private String username;
	private String password;

	private String email;
	private String mobile;
	private String sex;
	private String birthday;
	// 真实姓名
	private String name;
	// 身份证号
	private String idNo;
	private String accessKey;
	private String secretAccessKey;
	// 管理员标记
	private Boolean admin;

	private Set<String> roleIds;

	public AddUserParam() {
		super();
	}

	public UserPo toUserPo() {
		UserPo userPo = new UserPo();
		BeanUtils.copyProperties(this, userPo);
		return userPo;
	}
}
