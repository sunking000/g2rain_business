package com.g2rain.business.example.vo.param;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.example.po.ExamplePo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrUpdateExampleParam {
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

	public AddOrUpdateExampleParam() {
		super();
	}

	public ExamplePo toExamplePo() {
		ExamplePo examplePo = new ExamplePo();
		BeanUtils.copyProperties(this, examplePo);
		return examplePo;
	}
}
