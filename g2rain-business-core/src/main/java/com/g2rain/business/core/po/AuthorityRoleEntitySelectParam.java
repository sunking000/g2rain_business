package com.g2rain.business.core.po;


import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorityRoleEntitySelectParam extends BaseSelectParam {
	private String roleId;
	private String entityType;// COMPANY公司 STORE门店 USER用户
	private String organId;
	private String validStatus;
	private String entityId;

	public AuthorityRoleEntitySelectParam() {
		super();
	}
}
