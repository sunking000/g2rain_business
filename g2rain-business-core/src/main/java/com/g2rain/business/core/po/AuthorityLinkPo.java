package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.AuthorityLinkVo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorityLinkPo extends BasePo {
	private String linkId;
	private String name;
	private String code;
	private String linkPath;
	private String type;// MENU 菜单 ACTION 动作
	private String method;// GET POST PUT DELETE
	private int sort;
	private String parentLinkId;
	private String icon;

	public AuthorityLinkPo() {
		super();
	}

	public AuthorityLinkPo(AuthorityLinkVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
	}

}
