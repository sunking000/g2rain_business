package com.g2rain.business.gateway.filter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultExcludePathStrategy implements ExcludePathStrategy {

	private static Set<String> excludeApiPath = new HashSet<>();
	private static Set<String> excludeApiPathPattern = new HashSet<>();
	private static PathMatcher pathMatcher = new AntPathMatcher();

	static {
		excludeApiPath.add("/v2/api-docs");
		excludeApiPathPattern.add("/*/v2/api-docs");
	}

	@Override
	public boolean exclude(String contextPath, String apiPath) {
		log.info("contextPath:{}, path:{}", contextPath, apiPath);
		for (String pattern : excludeApiPathPattern) {
			if (pathMatcher.match(pattern, apiPath)) {
				return true;
			}
		}

		return excludeApiPath.contains(apiPath);
	}
}
