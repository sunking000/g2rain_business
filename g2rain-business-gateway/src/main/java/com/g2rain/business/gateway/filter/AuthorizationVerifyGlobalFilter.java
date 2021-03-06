package com.g2rain.business.gateway.filter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.g2rain.business.gateway.adapter.CoreClient;
import com.g2rain.business.gateway.adapter.domain.LoginToken;
import com.g2rain.business.gateway.auth.AuthBo;
import com.g2rain.business.gateway.exception.BussinessRuntimeException;
import com.g2rain.business.gateway.exception.ErrorCodeEnum;
import com.g2rain.business.gateway.rc.CommonContextContainer;
import com.g2rain.business.gateway.rc.Context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(1000)
@Service
public class AuthorizationVerifyGlobalFilter implements GlobalFilter, ExcludePathStrategy {
	@Autowired
	private AuthBo authBo;
	@Autowired
	private CoreClient coreClient;
	@Value("${debug.key}")
	private String debugKey;
	private static Set<String> verifyExcludeApiContext = new HashSet<>();
	private static Set<String> verifyExcludeApiPath = new HashSet<>();

	public AuthorizationVerifyGlobalFilter() {
		verifyExcludeApiContext.add("auth");
		verifyExcludeApiPath.add("/user_auth/login");
	}
	@Autowired
	private DefaultExcludePathStrategy defaultExcludePathStrategy;
	@Override
	public boolean exclude(String contextPath, String apiPath) {
		return defaultExcludePathStrategy.exclude(contextPath, apiPath)
				|| verifyExcludeApiContext.contains(contextPath) ||
				verifyExcludeApiPath.contains(apiPath);
	}



	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("filter:AuthorizationVerifyGlobalFilter");
		Context context = CommonContextContainer.getContext(exchange);
		if (exclude(context.getApiContextPath(), context.getApiPath())) {
			return chain.filter(exchange);
		}

		LoginToken loginToken = context.getLoginToken();
		if (loginToken == null) {
			loginToken = coreClient.getLoginToken(context.getAccessKey());
			if (loginToken == null) {
				throw new BussinessRuntimeException(ErrorCodeEnum.TOKEN_EXP_ERROR);
			}
			context.setLoginToken(loginToken);
		}

		boolean isDebug = false;
		String headerDebugKey = context.getDebugKey();
		if (StringUtils.isNotBlank(debugKey) && StringUtils.isNotBlank(headerDebugKey)
				&& debugKey.equals(headerDebugKey)) {
			log.info("debugKey:{}, headerDebugKey:{}", debugKey, headerDebugKey);
			isDebug = true;
		}

		if (!isDebug) {
			if (StringUtils.isBlank(context.getSign())) {
				throw new BussinessRuntimeException(ErrorCodeEnum.NO_SIGN);
			}
			if (!authBo.verifySign(context)) {
				log.error("签名错误");
				BussinessRuntimeException br = new BussinessRuntimeException(ErrorCodeEnum.SIGN_ERROR);
				if (context.isDebugFlag()) {
					br.addSubError("SIGN_PARAM_DATA", "paramString", authBo.getDataString(context));
				}

				throw br;
			}
		}
		return chain.filter(exchange);
	}
}
