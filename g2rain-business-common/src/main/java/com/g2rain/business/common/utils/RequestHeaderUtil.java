package com.g2rain.business.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.g2rain.business.common.enums.RequestHeaderKeyEnum;

public class RequestHeaderUtil {

	public static String getHeaderValue(HttpServletRequest request, RequestHeaderKeyEnum key) {
		if (request == null) {
			return null;
		}
		
		return request.getHeader(key.getUpper()) != null ? request.getHeader(key.getUpper())
				: request.getHeader(key.getLower());
	}
}