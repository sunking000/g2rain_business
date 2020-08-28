package com.g2rain.business.common.utils;

import org.apache.commons.lang.StringUtils;

public class CommonStringUtil {

	/**
	 * 格式化字符串为6位
	 * 
	 * @param string
	 * @return
	 */
	public static String formatSixChar(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		} else if (string.length() == 6) {
			return string;
		} else if (string.length() < 6) {
			StringBuffer sb = new StringBuffer();
			int i = 0;
			while (i < 6 - string.length()) {
				sb.append("0");
				i++;
			}
			sb.append(string);
			return sb.toString();
		} else {
			return string.substring(string.length() - 6, string.length());
		}
	}
}
