package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.OrganMembershipVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganMembershipPo extends BasePo {
	/**
	 * 父组织id
	 */
	private String parentOrganId;
	/**
	 * 孩子组织id
	 */
	private String childOrganId;
	/**
	 * 孩子组织类型
	 */
	private String childOrganType;

	public OrganMembershipPo() {
		super();
	}

	public OrganMembershipPo(OrganMembershipVo organMembership) {
		super();
		BeanUtils.copyProperties(organMembership, this);
		if (organMembership.getChildOrganType() != null) {
			this.childOrganType = organMembership.getChildOrganType().name();
		}
	}
}