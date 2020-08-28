package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.AuthorityRoleEntityVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleEntityPo extends BasePo {
	private String roleId;
	private String entityId;
	private String entityType;// COMPANY公司 STORE门店 USER用户
	private String organId;

	public AuthorityRoleEntityPo() {
		super();
	}

	public AuthorityRoleEntityPo(AuthorityRoleEntityVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
	}
}
