package com.g2rain.business.core.vo;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.AuthorityPermissionRolePo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityPermissionRoleVo extends BaseVo {
	private String permissionId;
	private String roleId;
	/**
	 * 许可类型LINK DICT
	 */
	private String permissionType;
	private String organId;

	public AuthorityPermissionRoleVo() {
		super();
	}

	public AuthorityPermissionRoleVo(AuthorityPermissionRolePo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
	}

}
