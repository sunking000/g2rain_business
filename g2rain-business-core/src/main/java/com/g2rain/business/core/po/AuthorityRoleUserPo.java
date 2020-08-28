package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.AuthorityRoleUserVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleUserPo extends BasePo {
	private String roleId;
	private String userId;
	private String organId;

	public AuthorityRoleUserPo() {
		super();
	}

	public AuthorityRoleUserPo(AuthorityRoleUserVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
	}

}
