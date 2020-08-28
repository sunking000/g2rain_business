package com.g2rain.business.common.bo;

import javax.servlet.http.HttpServletRequest;

public interface ServletPathPatternMatcher {

	public boolean isMatch(HttpServletRequest request);

	public default void addExcludePath(String... path) {
		
	};
}
