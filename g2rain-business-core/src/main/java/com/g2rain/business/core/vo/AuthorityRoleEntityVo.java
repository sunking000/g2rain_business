package com.g2rain.business.core.vo;
import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.AuthorityRoleEntityPo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleEntityVo extends BaseVo {
	private String roleId;
	private String entityId;
	private String entityType;// COMPANY公司 STORE门店 USER用户
	private String organId;

	public AuthorityRoleEntityVo() {
		super();
	}

	public AuthorityRoleEntityVo(AuthorityRoleEntityPo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
	}

}
