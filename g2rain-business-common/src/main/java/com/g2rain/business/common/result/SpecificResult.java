package com.g2rain.business.common.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties("data")
@Setter
@Getter
public class SpecificResult<T> extends BaseResult {

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
