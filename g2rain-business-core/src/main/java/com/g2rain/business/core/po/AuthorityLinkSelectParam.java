package com.g2rain.business.core.po;


import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorityLinkSelectParam extends BaseSelectParam {
	private String type;// MENU 菜单 ACTION 动作
	private String method;// GET POST PUT DELETE
	private String linkPath;
	private String linkId;
	private String parentLinkId;

	public AuthorityLinkSelectParam() {
		super();
	}
}
