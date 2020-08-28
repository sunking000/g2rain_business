package com.g2rain.business.common.exception;

import java.util.HashSet;
import java.util.Set;

import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SubError;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @ClassName exception
 * @Description TODO
 *
 * @author sunhaojie@kingsoft.com
 * @date 2017年7月18日 下午8:37:17
 */
@Getter
@Setter
public class BussinessRuntimeException extends RuntimeException {
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
		super(errorCode);
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
    	if(this.subErrors == null) {
    		this.subErrors = new HashSet<>();
    	}
    	
    	this.subErrors.add(subError);
    }

	public BaseResult convertResult() {
		ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.nameOf(this.errorCode);
		BaseResult result = new BaseResult();
		result.setStatus(errorCodeEnum == null ? 400 : errorCodeEnum.getStatus());
		result.setErrorCode(this.errorCode);
		// result.setMessage(resolveMessage(errorCodeEnum.getMessage(),
		// getPlaceHolder()));
		result.setSubErrors(subErrors);
		
		return result;
	}
}