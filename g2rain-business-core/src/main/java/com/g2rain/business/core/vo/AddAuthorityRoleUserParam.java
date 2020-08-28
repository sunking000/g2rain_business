package com.g2rain.business.core.vo;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.g2rain.business.core.po.AuthorityRoleUserPo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAuthorityRoleUserParam {
	private String roleId;
	private String userId;
	private String organId;
	private Set<String> roleIds;

	public AddAuthorityRoleUserParam() {
		super();
	}

	public AuthorityRoleUserPo toAuthorityRoleUserPo() {
		AuthorityRoleUserPo po = new AuthorityRoleUserPo();
		BeanUtils.copyProperties(this, po);
		return po;
	}

	public Set<AuthorityRoleUserPo> toAuthorityRoleUserPos() {
		if (CollectionUtils.isEmpty(roleIds)) {
			return null;
		}

		Set<AuthorityRoleUserPo> pos = new HashSet<>();
		for (String roleId : roleIds) {
			AuthorityRoleUserPo po = new AuthorityRoleUserPo();
			BeanUtils.copyProperties(this, po);
			po.setRoleId(roleId);
			pos.add(po);
		}

		return pos;
	}
}
