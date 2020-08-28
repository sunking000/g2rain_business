package com.g2rain.business.core.vo;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.UserPo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(value = { "password" }, allowSetters = true)
public class UserVo extends BaseVo {
	private String userId;
	private String organId;
	private String username;
	private String password;
	// 以下是Person信息
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
	private Boolean adminFlag;
	// 门店组织
	private OrganVo storeOrgan;


	public UserVo() {
		super();
	}

	public UserVo(UserPo user) {
		super(user);
		BeanUtils.copyProperties(user, this);
	}
}
