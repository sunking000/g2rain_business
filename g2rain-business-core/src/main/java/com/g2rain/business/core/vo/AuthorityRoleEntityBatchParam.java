package com.g2rain.business.core.vo;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleEntityBatchParam {
	private Set<String> entityIds;
	private Set<String> deleteEntityIds;
	private String roleId;
	private String entityType;// COMPANY公司 STORE门店 USER用户
	private String organId;

	public AuthorityRoleEntityBatchParam() {
		super();
	}
}