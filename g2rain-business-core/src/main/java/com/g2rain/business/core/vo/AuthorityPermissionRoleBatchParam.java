package com.g2rain.business.core.vo;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityPermissionRoleBatchParam {
	private Set<String> permissionIds;
	private Set<String> deletePermissionIds;
	private String roleId;
	/**
	 * 许可类型LINK DICT
	 */
	private String permissionType;
	private String organId;

	public AuthorityPermissionRoleBatchParam() {
		super();
	}
}
