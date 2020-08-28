package com.g2rain.business.common.enums;

public enum SignTypeEnum {
	IN("签到"), OUT("签出");

	private String showName;
	private SignTypeEnum(String showName) {
		this.showName = showName;
	}

	public String getShowName() {
		return showName;
	}
}