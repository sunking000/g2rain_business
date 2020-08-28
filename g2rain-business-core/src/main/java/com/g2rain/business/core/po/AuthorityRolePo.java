package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.AuthorityRoleVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRolePo extends BasePo {
	private String roleId;
	private String code;
	private String name;
	// SYSTEM 系统类型 USER 门店类型
	private String type;
	private String organId;

	public AuthorityRolePo() {
		super();
	}

	public AuthorityRolePo(AuthorityRoleVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
		if (vo.getType() != null) {
			this.type = vo.getType().name();
		}
	}
}
