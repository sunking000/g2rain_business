package com.g2rain.business.common.bo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class GetRequestMappingPathPattern implements ServletPathPatternMatcher {

	private String controllerPacage;
	private AbstractHandlerMethodMapping<RequestMappingInfo> requestMappingHandlerMapping;
	private Set<String> pathPatterns;
	private Set<String> excludePaths;
	private PathMatcher pathMatcher = new AntPathMatcher();

	public GetRequestMappingPathPattern(AbstractHandlerMethodMapping<RequestMappingInfo> requestMappingHandlerMapping,
			String controllerPacage) {
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
		this.controllerPacage = controllerPacage;
	}

	@Override
	public boolean isMatch(HttpServletRequest request) {
		String path = request.getServletPath();

		if (CollectionUtils.isNotEmpty(this.excludePaths)) {
			if (this.excludePaths.contains(path)) {
				return false;
			}
		}

		Set<String> patterns = getRequestMappingPathPatterns();
		if (CollectionUtils.isEmpty(patterns)) {
			return true;
		}
		
		for (String pathPattern : patterns) {
			if (pathMatcher.match(pathPattern, path)) {
				log.debug("match; path pattern:{}, path:{}", pathPattern, path);
				return true;
			}
		}

		return false;
	}

	public Set<String> getRequestMappingPathPatterns() {
		if (this.pathPatterns == null) {
			refresh();
		}
		return pathPatterns;
	}

	public Set<String> refresh() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		Set<String> pathPatterns = new HashSet<>();
		for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
			HandlerMethod handlerMethod = handlerMethods.get(requestMappingInfo);
			String controllerTypeName = handlerMethod.getBeanType().getName();
			if (controllerTypeName.indexOf(controllerPacage) > -1) {
				pathPatterns.addAll(requestMappingInfo.getPatternsCondition().getPatterns());
			}
		}
		
		this.pathPatterns = new HashSet<>();
		for (String temp : pathPatterns) {
			this.pathPatterns.add(temp.replaceAll("\\{[^}]*\\}", "\\*"));
		}

		return this.pathPatterns;
	}

	@Override
	public void addExcludePath(String... paths) {
		log.info("exclude path:{}", JSONObject.toJSONString(paths));
		if (ArrayUtils.isNotEmpty(paths)) {
			if (this.excludePaths == null) {
				this.excludePaths = new HashSet<>();
			}
			
			this.excludePaths.addAll(Arrays.asList(paths));
		}
	}
}
