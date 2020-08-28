package com.g2rain.business.common.enums;

public enum RequestHeaderKeyEnum {
	/**
	 * 请求id
	 */
	REQUEST_ID("X-REQUEST-ID", "x-request-id"),
	/**
	 * 请求时间
	 */
	REQUEST_TIME("X-REQUEST-TIME", "x-request-time"),
	/**
	 * SESSION类型
	 */
	SESSION_TYPE("X-SESSION-TYPE", "x-session-type"),
	/**
	 * 用户id
	 */
	USER_ID("X-USER-ID", "x-user-id"),
	/**
	 * 会员id
	 */
	MEMBER_ID("X-MEMBER-ID", "x-member-id"),
	/**
	 * 组织ID
	 */
	ORGAN_ID("X-ORGAN-ID", "x-organ-id"),
	/**
	 * 公司id
	 */
	ORGAN_TYPE("X-ORGAN-TYPE", "x-organ-type"),
	/**
	 * 公司内管理员
	 */
	ADMIN_USER("X-ADMIN-USER", "x-admin-user"),
	/**
	 * 管理公司
	 */
	ADMIN_COMPANY("X-ADMIN-COMPANY", "x-admin-company"),
	/**
	 * debug标记
	 */
	HOLD_STORE_ORGAN_IDS("X-HOLD-STORE", "x-hold-store"),
	/**
	 * debug标记
	 */
	DEBUG_FLAG("X-DEBUG-FLAG", "x-debug-flag"),
	/**
	 * debug标记
	 */
	BACK_EDN("X-BACK-END", "x-back-end");
	private String upper;
	private String lower;
	private RequestHeaderKeyEnum(String upper, String lower) {
		this.upper = upper;
		this.lower = lower;
	}

	public String getUpper() {
		return upper;
	}

	public String getLower() {
		return lower;
	}

}
