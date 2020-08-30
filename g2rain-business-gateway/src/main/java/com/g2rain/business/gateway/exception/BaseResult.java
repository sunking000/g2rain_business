package com.g2rain.business.gateway.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class BaseResult {
	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Set<SubError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(Set<SubError> subErrors) {
		this.subErrors = subErrors;
	}

	public static final BaseResult SUCCESS = new BaseResult(200, ErrorCodeEnum.SUCCESS.name(), "");

	public static final BaseResult BAD_REQUEST = new BaseResult(400, ErrorCodeEnum.BAD_REQUEST.name(), "");

	public static final BaseResult FAIL = new BaseResult(500, ErrorCodeEnum.SERVICE_ERROR.name(), "");

	private int status;

	private String errorCode;

	private String message;

	private String timestamp;

	private String path;

	private String method;

	private String requestId;

	private Map<String, Object> data;

	private Set<SubError> subErrors;

	public int getStatus() {
		return this.status;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public String getPath() {
		return this.path;
	}

	public String getMethod() {
		return this.method;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public Map<String, Object> getData() {
		return this.data;
	}

	public BaseResult(BaseResult baseResult) {
		this.errorCode = baseResult.getErrorCode();
		this.status = baseResult.getStatus();
	}

	public BaseResult() {
	}

	public BaseResult(int status, String errorCode, String message) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void putData(String key, Object value) {
		if (this.data == null)
			this.data = new HashMap<>();
		this.data.put(key, value);
	}

	public Object getData(String key) {
		if (this.data == null)
			return null;
		return this.data.get(key);
	}

	public ResponseEntity<BaseResult> toResponseEntity() {
		int status = (this.status > 0) ? this.status : 200;
		HttpStatus httpStatus = HttpStatus.valueOf(status);
		ResponseEntity<BaseResult> response = new ResponseEntity<BaseResult>(this, httpStatus);
		return response;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", Integer.valueOf(this.status));
		resultMap.put("errorCode", this.errorCode);
		resultMap.put("message", this.message);
		resultMap.put("timestamp", this.timestamp);
		resultMap.put("path", this.path);
		resultMap.put("method", this.method);
		resultMap.put("requestId", this.requestId);
		resultMap.put("data", this.data);
		resultMap.put("subErrors", this.subErrors);
		return resultMap;
	}
}
