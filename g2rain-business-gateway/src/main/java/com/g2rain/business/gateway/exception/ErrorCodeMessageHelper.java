package com.g2rain.business.gateway.exception;

import java.util.HashMap;
import java.util.Map;

import com.g2rain.business.gateway.utils.JsonObjectUtil;

public class ErrorCodeMessageHelper {

	private static Map<String, String> errorCodeMessages = new HashMap<String, String>();
	private static Map<String, String> subErrorCodeMessages = new HashMap<String, String>();

	static {
		errorCodeMessages.put("SUCCESS", "成功");
		errorCodeMessages.put("FAIL", "失败");
		errorCodeMessages.put("PARAMETER_ERROR", "参数错误");
		errorCodeMessages.put("SERVICE_ERROR", "服务器端错误");
		errorCodeMessages.put("DATE_FORMAT_ILLEGAL", "日期格式非法");
		errorCodeMessages.put("DATA_SOURCE_ERROR", "数据库异常");
		errorCodeMessages.put("USER_NOT_LOGIN", "用户未登陆");
		errorCodeMessages.put("TOKEN_EXP_ERROR", "用户未登陆");
		errorCodeMessages.put("DEVICE_TYPE_UNSUPPORTED", "设备类型不支持");
		errorCodeMessages.put("DEVICE_NON_EXISTENT", "设备不存在");
		errorCodeMessages.put("DEVICE_EXIST", "设备编码已存在");
		errorCodeMessages.put("STORE_TICKET_DATA_EXIST", "小票已存在");
		errorCodeMessages.put("STORE_TICKET_DATA_NON_EXISTENT", "小票不存在");
		errorCodeMessages.put("PRINTER_DEVICE_CONFIG_NON_EXISTENT", "打印机配置不存在");
		errorCodeMessages.put("USER_NOT_AUTH_SUBMIT_TICKET_DATA", "该用户不能上送小票");
		errorCodeMessages.put("DEVICE_TYPE_UNSUPPORTED", "设备类型不支持");
		errorCodeMessages.put("DEVICE_NON_EXISTENT", "设备不存在");
		errorCodeMessages.put("DEVICE_EXIST", "设备已存在");
		errorCodeMessages.put("STORE_TICKET_DATA_EXIST", "小票已存在");
		errorCodeMessages.put("STORE_TICKET_DATA_NON_EXISTENT", "小票不存在");
		errorCodeMessages.put("PRINTER_DEVICE_CONFIG_NON_EXISTENT", "打印机配置不存在");
		errorCodeMessages.put("ORGAN_STATUS_ERROR", "组织状态错误");
		errorCodeMessages.put("COMPANY_ORGAN_STATUS_ABNORMAL", "公司状态异常");
		errorCodeMessages.put("STORE_ORGAN_STATUS_ABNORMAL", "门店状态异常");
		errorCodeMessages.put("USER_NOT_AUTH_ORDER", "用户无权下单");
		errorCodeMessages.put("MEMBER_NOT_EXIST", "会员不存在");
		errorCodeMessages.put("ORDER_CANT_PAYED", "订单不能支付");
		errorCodeMessages.put("ORDER_CANT_CANCEL", "订单不能取消");
		errorCodeMessages.put("USER_NOT_EXIST_IN_THE_STORE", "门店下无用户");
		errorCodeMessages.put("STATUS_INVALID", "状态无效");
		errorCodeMessages.put("USER_EXIST", "用户已存在");
		errorCodeMessages.put("USER_NON_EXISTENT", "用户不存在");
		errorCodeMessages.put("USER_LOGIN_PARAM_ERROR", "用户登录参数错误");
		errorCodeMessages.put("USER_NOT_EXIST_OR_PASSWORD_ERROR", "用户不存在或者密码错误");
		errorCodeMessages.put("USER_INFO_ERROR", "用户信息错误");
		errorCodeMessages.put("DEVICE_USER_MISMATCH", "用户和设备不匹配");
		errorCodeMessages.put("DEVICE_NON_EXISTENT_OR_PASSWORD_ERROR", "设备不存在或者密码错误");
		errorCodeMessages.put("DEVICE_IS_DELETE", "设备已删除");
		errorCodeMessages.put("NAME_EXIST", "名称已存在");
		errorCodeMessages.put("DATA_EXIST", "数据已存在");
		errorCodeMessages.put("PRODUCT_ID_NOT_EXISTENT", "商品不存在");
		errorCodeMessages.put("STORE_TICKET_DATA_NOT_EXIST", "小票数据不存在");
		errorCodeMessages.put("STORE_TICKET_DATA_UPDATE_ERROR", "小票状态更新失败");
		errorCodeMessages.put("ORDER_SAVE_ERROR", "订单保存错误");
		errorCodeMessages.put("PARSE_ERROR", "解析错误");
		errorCodeMessages.put("KEYWORD_NOT_BLANK", "关键字不能为空");
		errorCodeMessages.put("KEY_ID_NOT_BLANK", "主键不能为空");
		errorCodeMessages.put("ANCHOR_MISS", "锚点未命中");
		subErrorCodeMessages.put("NOT_BLANK", "不能为空");
		subErrorCodeMessages.put("NOT_NULL", "不能为Null");
		subErrorCodeMessages.put("NOT_EMPTY", "集合不能为空");
	}

	public static void main(String[] args) {
		System.out.println("errorCodeMessages:" + JsonObjectUtil.toJson(errorCodeMessages));
		System.out.println("subErrorCodeMessages:" + JsonObjectUtil.toJson(subErrorCodeMessages));
	}
}
