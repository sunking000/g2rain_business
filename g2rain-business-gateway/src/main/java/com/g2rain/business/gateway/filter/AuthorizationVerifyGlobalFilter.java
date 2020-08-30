package com.g2rain.business.gateway.filter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import reactor.core.publisher.Mono;

@Order(1000)
@Service
public class AuthorizationVerifyGlobalFilter implements GlobalFilter {
	private static final Logger log = LoggerFactory.getLogger(AuthorizationVerifyGlobalFilter.class);

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

	public boolean requireVerify(Context context) {
		return (!verifyExcludeApiContext.contains(context.getApiContextPath())
				&& !verifyExcludeApiPath.contains(context.getApiPath()));
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Context context = CommonContextContainer.getContext(exchange);
		if (requireVerify(context)) {

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
		}
		return chain.filter(exchange);
  }
}
