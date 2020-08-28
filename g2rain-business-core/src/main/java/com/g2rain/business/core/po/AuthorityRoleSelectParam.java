package com.g2rain.business.core.po;

import java.util.List;

import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class AuthorityRoleSelectParam extends BaseSelectParam {
	// SYSTEM 系统类型 USER 门店类型
	private String type;
	private String organId;
	private String code;
	private List<String> roleIds;

	public AuthorityRoleSelectParam() {
		super();
	}
}