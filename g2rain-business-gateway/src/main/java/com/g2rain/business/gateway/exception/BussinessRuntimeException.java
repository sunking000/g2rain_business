package com.g2rain.business.gateway.exception;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @ClassName exception
 * @Description TODO
 *
 * @date 2017年7月18日 下午8:37:17
 */
@Getter
@Setter
public class BussinessRuntimeException extends RuntimeException {
	private Locale locale;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5899913949316638250L;
	/**
	 * 主错误码
	 */
	private String errorCode;
	/**
	 * 二级错误
	 */
	private Set<SubError> subErrors;

	public BussinessRuntimeException() {
		super();
	}

	public BussinessRuntimeException(ErrorCodeEnum errorCodeEnum) {
		super();
		this.errorCode = errorCodeEnum.name();
	}

	public BussinessRuntimeException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public BussinessRuntimeException(Throwable t) {
		super(t);
		this.errorCode = ErrorCodeEnum.SERVICE_ERROR.name();
	}

	public BussinessRuntimeException(String errorCode, Throwable t) {
		super(t);
		this.errorCode = errorCode;
	}

	public void addSubError(String errorCode, String key, String message) {
		if (this.subErrors == null) {
			this.subErrors = new HashSet<>();
		}

		this.subErrors.add(new SubError(errorCode, key, message));
	}

	public void addSubError(SubError subError) {
		if (this.subErrors == null) {
			this.subErrors = new HashSet<>();
		}

		this.subErrors.add(subError);
	}

	public BaseResult convertResult() {
		ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.nameOf(this.errorCode);
		if (errorCodeEnum == null) {
			errorCodeEnum = ErrorCodeEnum.SERVICE_ERROR;
		}
		BaseResult result = new BaseResult();
		result.setStatus(errorCodeEnum.getHttpStatus());
		result.setErrorCode(errorCodeEnum.name());
		// result.setMessage(resolveMessage(errorCodeEnum.getMessage(),
		// getPlaceHolder()));
		result.setSubErrors(subErrors);

		return result;
	}
}