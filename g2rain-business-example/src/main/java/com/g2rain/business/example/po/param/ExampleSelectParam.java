package com.g2rain.business.example.po.param;

import com.g2rain.business.common.domain.StoreBaseSelectParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExampleSelectParam extends StoreBaseSelectParam {
	/**
	 * 全局唯一主键
	 */
	private String exampleId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 名称模糊检索检索
	 */
	private String namePattern;

	public String getNamePattern() {
		return namePattern == null ? null : "%" + namePattern + "%";
	}

	public ExampleSelectParam() {
		super();
	}
}
