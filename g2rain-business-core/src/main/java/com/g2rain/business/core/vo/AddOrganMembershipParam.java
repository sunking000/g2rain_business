package com.g2rain.business.core.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrganMembershipParam {
	/**
	 * 父组织id
	 */
	private String parentOrganId;
	/**
	 * 孩子组织id
	 */
	private String childOrganId;

	public AddOrganMembershipParam() {
		super();
	}
}
