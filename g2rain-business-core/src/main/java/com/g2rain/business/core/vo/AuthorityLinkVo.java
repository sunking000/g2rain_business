package com.g2rain.business.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.core.po.AuthorityLinkPo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@JsonIgnoreProperties("subMenuSet")
@Getter
@Setter
public class AuthorityLinkVo extends BaseVo implements Serializable, Comparable<AuthorityLinkVo> {
	private static final long serialVersionUID = -5146906108551233382L;
	private String linkId;
	private String name;
	private String code;
	private String linkPath;
	private String type;// MENU 菜单 ACTION 动作
	private String method;// GET POST PUT DELETE
	// private String validStatus;
	private int sort;
	private String parentLinkId;
	/**
	 * 图标
	 */
	private String icon;

	private Set<AuthorityLinkVo> subMenuSet;

	public AuthorityLinkVo() {
		super();
	}

	public AuthorityLinkVo(AuthorityLinkPo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
	}


	public List<AuthorityLinkVo> getSubMenus() {
		if (subMenuSet == null) {
			return null;
		}
		List<AuthorityLinkVo> subMenus = new ArrayList<AuthorityLinkVo>(subMenuSet);
		if (CollectionUtils.isNotEmpty(subMenus) && subMenus.size() > 1) {
			Collections.sort(subMenus);
		}

		return subMenus;
	}

	@Override
	public int compareTo(AuthorityLinkVo o) {
		return this.sort > o.getSort() ? 1 : -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkId == null) ? 0 : linkId.hashCode());
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
		AuthorityLinkVo other = (AuthorityLinkVo) obj;
		if (linkId == null) {
			if (other.linkId != null)
				return false;
		} else if (!linkId.equals(other.linkId))
			return false;
		return true;
	}
}
