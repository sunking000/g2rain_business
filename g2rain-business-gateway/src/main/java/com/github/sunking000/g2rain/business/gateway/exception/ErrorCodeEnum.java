package com.github.sunking000.g2rain.business.gateway.exception;

import java.util.Locale;

public enum ErrorCodeEnum {
	SUCCESS("成功", "success",
			200),
	SERVICE_ERROR("服务端错误", "Server error.",
			500),
	TOKEN_EXP_ERROR("token过期", "toke expire", 401),
	BAD_REQUEST("页面不存在", "Bad request.", 400),
	PARAMETER_ERROR("参数错误", "Invalid parameter.", 400),
	NO_SIGN("签名信息不存在", "No sign parameter.", 401),
	NO_OPERATION_PERMISSION("无操作权限", "No operation permission.", 401),
	SIGN_ERROR("签名错误", "Sign error.", 401);
  
  private String simpleChineseMessage;
  
  private String englishMessage;
  
	private int httpStatus;
  
  public static ErrorCodeEnum nameOf(String name) {
    ErrorCodeEnum[] errorCodeEnums = values();
    for (ErrorCodeEnum item : errorCodeEnums) {
      if (item.name().equals(name))
        return item; 
      if (item.name().lastIndexOf("$") > 0) {
        String parameterNamePattern = name.substring(0, name.lastIndexOf("_"));
        String itemNamePattern = item.name().substring(0, item.name().lastIndexOf("_"));
        if (itemNamePattern.equals(parameterNamePattern))
          return item; 
      } 
    } 
    return null;
  }
  
  public String getMessage(Locale locale) {
    if (locale != null && locale.getLanguage().equals(Locale.ENGLISH.getLanguage()))
      return this.englishMessage; 
    return this.simpleChineseMessage;
  }
  
  public int getHttpStatus() {
    return this.httpStatus;
  }
  
  ErrorCodeEnum(String simpleChineseMessage, String englishMessage, int httpStatus) {
    this.simpleChineseMessage = simpleChineseMessage;
    this.englishMessage = englishMessage;
    this.httpStatus = httpStatus;
  }
}
