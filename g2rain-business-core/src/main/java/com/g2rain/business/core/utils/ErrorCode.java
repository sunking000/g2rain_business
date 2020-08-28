package com.g2rain.business.core.utils;

public enum ErrorCode {
	// 组织不存在
	ORGAN_NON_EXISTENT,
	// 组织状态错误
	ORGAN_STATUS_ERROR,
	// 公司状态异常
	COMPANY_ORGAN_STATUS_ABNORMAL,
	// 门店状态异常
	STORE_ORGAN_STATUS_ABNORMAL,
	// 用户无权下单
	USER_NOT_AUTH_ORDER,
	// 会员不存在
	MEMBER_NOT_EXIST,
	// 订单不能支付
	ORDER_CANT_PAYED,
	// 订单不能取消
	ORDER_CANT_CANCEL,
	// 门店下无用户
	USER_NOT_EXIST_IN_THE_STORE,
	// 状态无效
	STATUS_INVALID,
	// 用户已存在
	USER_EXIST,
	// 用户不存在
	USER_NON_EXISTENT,
	// 用户登录参数错误
	USER_LOGIN_PARAM_ERROR,
	// 用户不存在或者密码错误
	USER_NOT_EXIST_OR_PASSWORD_ERROR,
	// 用户信息错误
	USER_INFO_ERROR,
	// 用户和设备不匹配
	DEVICE_USER_MISMATCH,
	// 设备不存在或者密码错误
	DEVICE_NON_EXISTENT_OR_PASSWORD_ERROR,
	// 设备已删除
	DEVICE_IS_DELETE,
	// 名称已存在
	NAME_EXIST,
	// 公司和门店必须不能存在
	COMPANY_STORE_ORGAN_MUST_NOT,
	// 公司必须存在
	COMPANY_ORGAN_PARAMETER_MUST_EXIST,
	// 角色不能添加当前角色
	ROLE_CANNOT_CONTAIN_SELF,
	// 角色Code已存在
	ROLE_CODE_EXIST
}
