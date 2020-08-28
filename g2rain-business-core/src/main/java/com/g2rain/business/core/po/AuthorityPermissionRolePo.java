package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.AuthorityPermissionRoleVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityPermissionRolePo extends BasePo {
	private String permissionId;
	private String roleId;
	/**
	 * 许可类型LINK DICT ROLE
	 */
	private String permissionType;
	private String organId;

	public AuthorityPermissionRolePo() {
		super();
	}

	public AuthorityPermissionRolePo(AuthorityPermissionRoleVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
	}
}
