package com.g2rain.business.gateway.filter;

public interface ExcludePathStrategy {

	/**
	 * 判断是否排除
	 * 
	 * @param contextPath
	 * @param apiPath
	 * @return
	 */
	public boolean exclude(String contextPath, String apiPath);
}
