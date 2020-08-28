package com.g2rain.business.core.po;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorityRoleLinkSelectParam extends AuthorityLinkSelectParam {
	private String roleId;
	private List<String> roleIds;

	public AuthorityRoleLinkSelectParam() {
		super();
	}

}
