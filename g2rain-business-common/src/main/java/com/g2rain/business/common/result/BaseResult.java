package com.g2rain.business.common.result;

import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName BaseResult
 * @Description 基础结果值
 *
 * @author sunhaojie sunhaojie@kingsoft.com
 * @date 2016年8月21日 下午8:13:49
 */
@Getter
@Setter
public class BaseResult {
	
	/**
	 * 成功返回值常量
	 */
    public final static BaseResult SUCCESS = new BaseResult(200, "SUCCESS", "操作成功");
    public final static BaseResult FAIL = new BaseResult(500, "FAIL", "操作失败");
	public final static BaseResult PARAMETER_ERROR = new BaseResult(400, "PARAMETER_ERROR", "参数错误");

	/**
	 * 状态
	 */
	private int status;
	/**
	 * 错误码
	 */
	private String errorCode;
	/**
	 * 错误信息
	 */
	private String message;
	
    /**
     * 服务器端时间
     */
    private Date timestamp;
    /**
     * 访问路径
     */
    private String path;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求id
     */
    private String requestId;

	/**
	 * 二级错误
	 */
	private Set<SubError> subErrors;
	
	public BaseResult(BaseResult baseResult) {
		super();
		this.status = baseResult.getStatus();
		this.errorCode = baseResult.getErrorCode();
		this.message = baseResult.getMessage();
	}

	public BaseResult() {
		super();
	}

	/**
	 * @param status
	 * @param errorCode
	 * @param message
	 */
	public BaseResult(int status, String errorCode, String message) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}
}
