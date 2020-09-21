package com.g2rain.business.example.vo.param;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.example.po.ExamplePo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrUpdateExampleParam {
	@NotBlank
	private String storeOrganId;
	/**
	 * 全局唯一主键
	 */
	private String exampleId;
	/**
	 * 名称
	 */
	@NotBlank
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
