package com.g2rain.business.common.enums;

/**
 *
 * @ClassName ErrorCodeEnum
 * @Description 错误码枚举
 *
 * @date 2017年7月25日 上午11:10:04
 */
public enum ErrorCodeEnum {
	SUCCESS(200, "成功"),
	FAIL(500, "失败"),
	/**
	 * 数据库异常
	 */
	DATA_SOURCE_ERROR(500, "数据库异常"),
	/**
	 * 服务器端错误
	 */
	SERVICE_ERROR(500, "服务器端错误"),
	/**
	 * 参数错误
	 */
	PARAMETER_ERROR(400, "参数错误"),
	/**
	 * 文件读取错误
	 */
	STREAM_READER_ERROR(400, "文件流数据读取错误"),
    /**
     * io错误
     */
	HTTP_CLIENT_IO_ERROR(400, "服务器端错误"),
    /**
     * 编码错误
     */
	HTTP_CLIENT_ENCODING_ERROR(400, "编码错误"),
    /**
     * 未收到应答错误
     */
	HTTP_CLIENT_NO_RESPONSE_ERROR(400, "未收到应答错误"),
    /**
     * 状态错误
     */
	HTTP_CLIENT_STATUS_ERROR(400, "状态错误"),
    /**
     * 方法存在
     */
	HTTP_CLIENT_METHOD_NOT_EXIST(400, "方法存在"),
    /**
     * 路径错误
     */
	HTTP_CLIENT_URI_PATH_ERROR(400, "路径错误"),
    /**
     * 请求错误
     */
	HTTP_CLIENT_REQUEST_ERROR(400, "请求错误"),
    /**
     * 请求入参错误
     */
	HTTP_CLIENT_REQUEST_PARAM_ERROR(400, "请求入参错误"),
    /**
     * 不支持该请求方法
     */
	HTTP_CLIENT_METHOD_NOT_SUPPORT(400, "不支持该请求方法"),
    /**
     * 日期格式非法
     */
	DATE_FORMAT_ILLEGAL(400, "日期格式非法"),
	/**
	 * 数据不存在
	 */
	DATA_NON_EXISTENT(400, "数据不存在"),
	/**
	 * 用户未登陆
	 */
	USER_NOT_LOGIN(400, "用户未登陆");

	public static ErrorCodeEnum nameOf(String errorCode) {
		ErrorCodeEnum[] errorCodeEnums = ErrorCodeEnum.values();
		for (ErrorCodeEnum item : errorCodeEnums) {
			if (item.name().equals(errorCode)) {
				return item;
			} else if (item.name().lastIndexOf("$") > 0) {
				String parameterNamePattern = errorCode.substring(0, errorCode.lastIndexOf("_"));
				String itemNamePattern = item.name().substring(0, item.name().lastIndexOf("_"));
				if (parameterNamePattern.equals(itemNamePattern)) {
					return item;
				}
			}
		}

		return null;
	}

	private int status;
	private String message;

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	private ErrorCodeEnum(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
