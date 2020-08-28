package com.github.sunking000.g2rain.business.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-9000)
@Service
public class LogRequestGlobalFilter implements GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String requestParameter = (String) exchange
				.getAttribute(CacheRequestBodyGlobalFilter.CACHE_PARAMETER_STRING_KEY);
		if (StringUtils.isNotBlank(requestParameter))
			log.debug("requestParameter:{}", requestParameter);
		return chain.filter(exchange);
	}
}
