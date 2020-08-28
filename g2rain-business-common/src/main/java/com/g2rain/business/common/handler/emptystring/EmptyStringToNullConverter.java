package com.g2rain.business.common.handler.emptystring;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class EmptyStringToNullConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return source;
	}

}
