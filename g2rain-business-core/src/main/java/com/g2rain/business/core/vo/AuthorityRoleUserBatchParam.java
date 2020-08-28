package com.g2rain.business.core.vo;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleUserBatchParam {
	private Set<String> userIds;
	private Set<String> deleteUserIds;
	private String roleId;
	private String organId;

	public AuthorityRoleUserBatchParam() {
		super();
	}
}
