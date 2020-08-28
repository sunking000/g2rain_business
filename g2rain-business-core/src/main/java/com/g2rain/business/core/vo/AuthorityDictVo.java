package com.g2rain.business.core.vo;

import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.AuthorityDictPo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityDictVo extends BaseVo {
	private String dictId;
	// 类型
	private String type;
	private String name;
	private String code;
	// 父id
	private String parentDictId;
	private String pageCode;
	private Set<AuthorityDictVo> children;

	public AuthorityDictVo() {
		super();
	}

	public AuthorityDictVo(AuthorityDictPo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dictId == null) ? 0 : dictId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorityDictVo other = (AuthorityDictVo) obj;
		if (dictId == null) {
			if (other.dictId != null)
				return false;
		} else if (!dictId.equals(other.dictId))
			return false;
		return true;
	}
}
