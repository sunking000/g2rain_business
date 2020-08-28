package com.g2rain.business.common.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("具体结果类，结果值在resultData中")
@JsonIgnoreProperties("data")
@Setter
@Getter
public class SpecificResult<T> extends BaseResult {

	@ApiModelProperty("业务结果")
	private T resultData;

	public SpecificResult(BaseResult baseResult) {
		super();
		this.setStatus(baseResult.getStatus());
		this.setErrorCode(baseResult.getErrorCode());
		this.setMessage(baseResult.getMessage());
	}

	public SpecificResult() {
		super();
	}
}
