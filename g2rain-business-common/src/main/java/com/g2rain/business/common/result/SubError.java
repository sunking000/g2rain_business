package com.g2rain.business.common.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubError {
	private String errorCode;
	private String key;
	private String message;

	public SubError() {
		super();
	}

	public SubError(String errorCode, String key, String message) {
		super();
		this.errorCode = errorCode;
		this.key = key;
		this.message = message;
	}

	@Override
	public int hashCode() {
		if (errorCode != null && key != null) {
			return (errorCode + key).hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}
}
