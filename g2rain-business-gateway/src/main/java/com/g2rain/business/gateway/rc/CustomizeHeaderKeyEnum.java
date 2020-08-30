package com.g2rain.business.gateway.rc;

public enum CustomizeHeaderKeyEnum {
	/**
	 * SESSION类型
	 */
	SESSION_TYPE("X-SESSION-TYPE", "x-session-type"),
	/**
	 * 请求id
	 */
	REQUEST_ID("X-REQUEST-ID", "x-request-id"),
	/**
	 * 请求时间
	 */
	REQUEST_TIME("X-REQUEST-TIME", "x-request-time"),
	/**
	 * access key
	 */
	ACCESS_KEY("X-ACCESS-KEY", "x-access-key"),
	/**
	 * sign
	 */
	SIGN("X-REQUEST-SIGN", "x-request-sign"),
	/**
	 * debug key
	 */
	DEBUG_KEY("X-DEBUG-KEY", "x-debug-key"),
	/**
	 * 用户id
	 */
	USER_ID("X-USER-ID", "x-user-id"),
	/**
	 * 会员id
	 */
	MEMBER_ID("X-MEMBER-ID", "x-member-id"),
	/**
	 * 组织id
	 */
	ORGAN_ID("X-ORGAN-ID", "x-organ-id"),
	/**
	 * 公司内管理员
	 */
	ADMIN_USER("X-ADMIN-USER", "x-admin-user"),
	/**
	 * 管理公司
	 */
	ADMIN_COMPANY("X-ADMIN-COMPANY", "x-admin-company"),
	/**
	 * 持有门店id
	 */
	HOLD_STORE_ORGAN_IDS("X-HOLD-STORE", "x-hold-store"),
	/**
	 * 组织类型
	 */
	ORGAN_TYPE("X-ORGAN-TYPE", "x-organ-type"),
	/**
	 * debug标记
	 */
	DEBUG_FLAG("X-DEBUG-FLAG", "x-debug-flag"),
	/**
	 * 后端调用，跳过登录检查
	 */
	BACK_EDN("X-BACK-END", "x-back-end"),
	/**
	 * 版本
	 */
	API_VERSION("X-API-VERSION", "x-api-version");

	private String upper;

	private String lower;

	CustomizeHeaderKeyEnum(String upperKey, String lowerKey) {
		this.upper = upperKey;
		this.lower = lowerKey;
	}

	public String getUpper() {
		return this.upper;
	}

	public String getLower() {
		return this.lower;
	}
}
