package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.UserVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPo extends BasePo {
	private String userId;
	private String organId;
	private String username;
	private String password;
	private String accessKey;
	private String secretAccessKey;
	private String email;
	private String mobile;
	private String sex;
	private String birthday;
	// 真实姓名
	private String name;
	// 身份证号
	private String idNo;
	private boolean adminFlag = false;

	public UserPo() {
		super();
	}

	public UserPo(UserVo user) {
		super();
		BeanUtils.copyProperties(user, this);
	}
}
