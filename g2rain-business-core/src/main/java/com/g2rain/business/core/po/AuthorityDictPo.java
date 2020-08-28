package com.g2rain.business.core.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.AuthorityDictVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityDictPo extends BasePo {
	private String dictId;
	// 类型
	private String type;
	private String name;
	private String code;
	// 父id
	private String parentDictId;
	private String pageCode;

	public AuthorityDictPo() {
		super();
	}

	public AuthorityDictPo(AuthorityDictVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
	}
}