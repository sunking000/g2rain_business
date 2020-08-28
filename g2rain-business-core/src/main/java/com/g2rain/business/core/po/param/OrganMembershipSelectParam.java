package com.g2rain.business.core.po.param;

import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganMembershipSelectParam extends BaseSelectParam {
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


	public OrganMembershipSelectParam() {
		super();
	}
}
