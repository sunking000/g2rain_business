package com.g2rain.business.core.vo;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.utils.AuthorityRoleTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleVo extends BaseVo {
	private String roleId;
	private String code;
	private String name;
	// SYSTEM 系统类型； SPECIAL 公司门店级别
	@NotNull
	private AuthorityRoleTypeEnum type;
	private String organId;

	public AuthorityRoleVo() {
		super();
	}

	public AuthorityRoleVo(AuthorityRolePo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
		if (StringUtils.isNotBlank(po.getType())) {
			AuthorityRoleTypeEnum typeEnum = AuthorityRoleTypeEnum.valueOf(po.getType());
			this.type = typeEnum;
		}
	}
}
