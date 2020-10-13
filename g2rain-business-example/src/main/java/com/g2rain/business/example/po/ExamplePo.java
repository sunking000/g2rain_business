package com.g2rain.business.example.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.StoreOrganBasePo;
import com.g2rain.business.example.vo.ExampleVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamplePo extends StoreOrganBasePo {
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
	/**
	 * 内容
	 */
	private String content;

	public ExamplePo() {
		super();
	}

	public ExamplePo(ExampleVo example) {
		super();
		BeanUtils.copyProperties(example, this);
	}
}
