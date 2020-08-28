package com.g2rain.business.core.po;


import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AuthorityRoleUserSelectParam extends BaseSelectParam {
	private String roleId;
	private String userId;
	private String organId;

	public AuthorityRoleUserSelectParam() {

	}
}