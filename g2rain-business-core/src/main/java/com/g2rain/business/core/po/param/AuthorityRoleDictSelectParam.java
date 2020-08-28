package com.g2rain.business.core.po.param;

import java.util.List;

import com.g2rain.business.core.po.AuthorityDictSelectParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRoleDictSelectParam extends AuthorityDictSelectParam {
	private String roleId;
	private List<String> roleIds;

	public AuthorityRoleDictSelectParam() {
		super();
	}
}