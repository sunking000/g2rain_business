package com.g2rain.business.gateway.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.g2rain.business.gateway.rc.CommonContextContainer;
import com.g2rain.business.gateway.rc.Context;
import com.g2rain.business.gateway.utils.JsonObjectUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(10)
@Service
public class CacheRequestBodyGlobalFilter implements GlobalFilter, ExcludePathStrategy {
	public static final String CACHE_PARAMETER_STRING_KEY = "cachedParameterString";
	@Autowired
	private DefaultExcludePathStrategy defaultExcludePathStrategy;

	@Override
	public boolean exclude(String contextPath, String apiPath) {
		return defaultExcludePathStrategy.exclude(contextPath, apiPath);
	}
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("filter:CacheRequestBodyGlobalFilter");
		Context context = CommonContextContainer.getContext(exchange);
		if (exclude(context.getApiContextPath(), context.getApiPath())) {
			return chain.filter(exchange);
		}

		ServerHttpRequest request = exchange.getRequest();
		HttpMethod method = request.getMethod();
		String paramString = null;
		HttpHeaders headers = request.getHeaders();
		MediaType contentType = headers.getContentType();
		if (HttpMethod.GET.equals(method) || HttpMethod.DELETE.equals(method)) {
			MultiValueMap<String, String> queryParams = request.getQueryParams();
			Map<String, String> params = new HashMap<>();
			for (String parameterName : queryParams.keySet()) {
				List<String> valueList = queryParams.get(parameterName);
				String parameterValue = null;
				if (CollectionUtils.isNotEmpty(valueList)) {
					parameterValue = String.join(",", valueList);
				}
				params.put(parameterName, parameterValue);
			}
			paramString = JsonObjectUtil.toJson(params);
		} else if ((HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method))
				&& MediaType.APPLICATION_JSON.getType().equals(contentType.getType())) {
			String requestBody = exchange.getAttribute("cachedRequestBodyObject");
			paramString = requestBody;
		}
		if (paramString != null) {
			exchange.getAttributes().put(CACHE_PARAMETER_STRING_KEY, paramString);
		}

		return chain.filter(exchange);
	}
}
