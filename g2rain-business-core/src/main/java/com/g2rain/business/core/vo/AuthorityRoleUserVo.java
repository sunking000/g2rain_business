package com.g2rain.business.core.vo;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.AuthorityRoleUserPo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleUserVo extends BaseVo {
	private String roleId;
	private String userId;
	private String organId;

	public AuthorityRoleUserVo() {
		super();
	}

	public AuthorityRoleUserVo(AuthorityRoleUserPo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
	}

}
