package com.g2rain.business.gateway.filter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class DefaultExcludePathStrategy implements ExcludePathStrategy {

	private static Set<String> excludeApiPath = new HashSet<>();

	static {
		excludeApiPath.add("/v2/api-docs");
	}

	@Override
	public boolean exclude(String contextPath, String apiPath) {
		return excludeApiPath.contains(apiPath);
	}
}
