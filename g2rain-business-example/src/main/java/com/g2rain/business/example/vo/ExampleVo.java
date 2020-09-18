package com.g2rain.business.example.vo;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.StoreOrganBaseVo;
import com.g2rain.business.example.po.ExamplePo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExampleVo extends StoreOrganBaseVo {
	/**
	 * 全局唯一主键
	 */
	private String exampleId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;

	public ExampleVo() {
		super();
	}

	public ExampleVo(ExamplePo example) {
		super(example);
		BeanUtils.copyProperties(example, this);
	}
}
