package com.g2rain.business.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.g2rain.business.gateway.auth.HoldStoreIdCacheBo;
import com.g2rain.business.gateway.rc.CommonContextContainer;
import com.g2rain.business.gateway.rc.Context;
import com.g2rain.business.gateway.rc.CustomizeHeaderKeyEnum;
import com.g2rain.business.gateway.utils.OrganTypeEnum;

import reactor.core.publisher.Mono;

@Order(2000)
@Service
public class AddInfoToHeaderGlobalFilter implements GlobalFilter {

	@Autowired
	private HoldStoreIdCacheBo holdStoreIdCacheBo;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Context context = CommonContextContainer.getContext(exchange);
		ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
		builder = addHeader(builder, CustomizeHeaderKeyEnum.SESSION_TYPE, context.getSessionType());
		if (StringUtils.isNotBlank(context.getUserId())) {
			builder = addHeader(builder, CustomizeHeaderKeyEnum.USER_ID, context.getUserId());
		}
		if (StringUtils.isNotBlank(context.getMemberId())) {
			builder = addHeader(builder, CustomizeHeaderKeyEnum.MEMBER_ID, context.getMemberId());
		}
		if (StringUtils.isNotBlank(context.getOrganId())) {
			builder = addHeader(builder, CustomizeHeaderKeyEnum.ORGAN_ID, context.getOrganId());
			builder = addHeader(builder, CustomizeHeaderKeyEnum.ORGAN_TYPE, context.getOrganType());

			if (OrganTypeEnum.COMPANY.name().equals(context.getOrganType())) {
				String holdStoreIds = holdStoreIdCacheBo.holdStoreIds(context.getOrganId());
				if (StringUtils.isNotBlank(holdStoreIds)) {
					builder = addHeader(builder, CustomizeHeaderKeyEnum.HOLD_STORE_ORGAN_IDS, holdStoreIds);
				}
			}
		}

		builder = addHeader(builder, CustomizeHeaderKeyEnum.ADMIN_USER, context.isAdminUser() + "");
		builder = addHeader(builder, CustomizeHeaderKeyEnum.ADMIN_COMPANY, context.isAdminCompany() + "");
		builder = addHeader(builder, CustomizeHeaderKeyEnum.DEBUG_FLAG, context.isDebugFlag() + "");

		ServerHttpRequest serverHttpRequest = builder.build();
		ServerWebExchange webExchange = exchange.mutate().request(serverHttpRequest).build();
		return chain.filter(webExchange);
	}

	private ServerHttpRequest.Builder addHeader(ServerHttpRequest.Builder builder, CustomizeHeaderKeyEnum headerKey,
			String value) {
		if (StringUtils.isNotBlank(value))
			builder = builder.header(headerKey.getLower(), value);
		return builder;
	}
}
