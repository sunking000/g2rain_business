package com.g2rain.business.core.po;



import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.ToString;

@ToString
public class AuthorityDictSelectParam extends BaseSelectParam {
	// 类型
	private String type;
	private String code;
	// 父id
	private String parentDictId;
	private String pageCode;

	public AuthorityDictSelectParam() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentDictId() {
		return parentDictId;
	}

	public void setParentDictId(String parentDictId) {
		this.parentDictId = parentDictId;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}
}