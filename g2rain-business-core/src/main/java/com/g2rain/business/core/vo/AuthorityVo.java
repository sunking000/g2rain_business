package com.g2rain.business.core.vo;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties("roles")
@Setter
@Getter
public class AuthorityVo {

	@ApiModelProperty("角色集合")
	private List<AuthorityRoleVo> roles;
	// @ApiModelProperty
	// private List<AuthorityLinkVo> actions;
	@ApiModelProperty("菜单集合")
	private List<AuthorityLinkVo> menus;
	@ApiModelProperty("字典集合")
	private List<AuthorityDictVo> dicts;

	public AuthorityVo() {
		super();
	}

	public void putMenu(AuthorityLinkVo menu) {
		if (menus == null) {
			this.menus = new LinkedList<AuthorityLinkVo>();
		}
		this.menus.add(menu);
	}

	public void putAction(AuthorityDictVo dict) {
		if (dicts == null) {
			this.dicts = new LinkedList<AuthorityDictVo>();
		}
		this.dicts.add(dict);
	}

	public void putRole(AuthorityRoleVo role) {
		if (roles == null) {
			this.roles = new LinkedList<AuthorityRoleVo>();
		}
		this.roles.add(role);
	}
}