package com.g2rain.business.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.g2rain.business.gateway.rc.CommonContextContainer;
import com.g2rain.business.gateway.rc.Context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-9000)
@Service
public class LogRequestGlobalFilter implements GlobalFilter, ExcludePathStrategy {
	@Autowired
	private DefaultExcludePathStrategy defaultExcludePathStrategy;

	@Override
	public boolean exclude(String contextPath, String apiPath) {
		return defaultExcludePathStrategy.exclude(contextPath, apiPath);
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Context context = CommonContextContainer.getContext(exchange);
		if (exclude(context.getApiContextPath(), context.getApiPath())) {
			return chain.filter(exchange);
		}

		String requestParameter = (String) exchange
				.getAttribute(CacheRequestBodyGlobalFilter.CACHE_PARAMETER_STRING_KEY);
		if (StringUtils.isNotBlank(requestParameter))
			log.debug("requestParameter:{}", requestParameter);
		return chain.filter(exchange);
	}
}
