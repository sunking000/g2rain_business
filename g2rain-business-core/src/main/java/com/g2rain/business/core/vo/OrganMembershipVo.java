package com.g2rain.business.core.vo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.core.po.OrganMembershipPo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrganMembershipVo extends BaseVo {
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
	private OrganTypeEnum childOrganType;

	public OrganMembershipVo() {
		super();
	}

	public OrganMembershipVo(OrganMembershipPo organMembership) {
		super(organMembership);
		BeanUtils.copyProperties(organMembership, this);
		if (StringUtils.isNotBlank(organMembership.getChildOrganType())) {
			this.childOrganType = OrganTypeEnum.valueOf(organMembership.getChildOrganType());
		}
	}
}
