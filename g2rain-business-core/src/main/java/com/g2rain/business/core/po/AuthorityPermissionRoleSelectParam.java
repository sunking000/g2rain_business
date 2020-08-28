package com.g2rain.business.core.po;

import java.util.List;

import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthorityPermissionRoleSelectParam extends BaseSelectParam {
	private String roleId;
	/**
	 * 许可类型LINK DICT
	 */
	private String permissionType;
	private String organId;
	private String permissionId;
	private List<String> roleIds;

	public AuthorityPermissionRoleSelectParam() {
		super();
	}

}
