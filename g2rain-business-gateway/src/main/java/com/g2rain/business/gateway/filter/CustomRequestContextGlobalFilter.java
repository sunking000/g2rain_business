package com.g2rain.business.gateway.filter;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.g2rain.business.gateway.rc.CommonContextContainer;
import com.g2rain.business.gateway.rc.Context;
import com.g2rain.business.gateway.rc.CustomizeHeaderKeyEnum;

import reactor.core.publisher.Mono;

@Order(0)
@Service
public class CustomRequestContextGlobalFilter implements GlobalFilter, ExcludePathStrategy {
	private static final Logger log = LoggerFactory.getLogger(CustomRequestContextGlobalFilter.class);
	@Autowired
	private DefaultExcludePathStrategy defaultExcludePathStrategy;

	@Override
	public boolean exclude(String contextPath, String apiPath) {
		return defaultExcludePathStrategy.exclude(contextPath, apiPath);
	}

	private String getHeaderValue(ServerHttpRequest request, HttpHeaders headers, CustomizeHeaderKeyEnum key) {
		List<String> list = (headers.get(key.getUpper()) == null) ? headers.get(key.getLower())
				: headers.get(key.getUpper());
		String headerValue = (list == null) ? null : String.join(",", list);
		if (StringUtils.isBlank(headerValue)) {
			MultiValueMap<String, String> valueMap = request.getQueryParams();
			headerValue = (valueMap.getFirst(key.getUpper()) == null) ? (String) valueMap.getFirst(key.getLower())
					: (String) valueMap.getFirst(key.getUpper());
		}
		return headerValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("filter:CustomRequestContextGlobalFilter");
		Context context = new Context();
		CommonContextContainer.setContext(exchange, context);

		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = exchange.getRequest().getHeaders();

		// debug flag
		String debugFlagString = getHeaderValue(request, headers, CustomizeHeaderKeyEnum.DEBUG_FLAG);
		log.debug("debugFlagString:{}", debugFlagString);
		context.setDebugFlag(debugFlagString == null ? false : Boolean.valueOf(debugFlagString));

		LocaleContext localeContext = exchange.getLocaleContext();
		Locale locale = (localeContext.getLocale() == null) ? Locale.SIMPLIFIED_CHINESE : localeContext.getLocale();
		context.setLocale(locale);

		// request id
		String requestId = getHeaderValue(request, headers, CustomizeHeaderKeyEnum.REQUEST_ID);
		log.debug("requestId:{}", requestId);
		context.setRequestId(requestId);

		// request time
		String requestTime = getHeaderValue(request, headers, CustomizeHeaderKeyEnum.REQUEST_TIME);
		log.debug("requestTime:{}", requestTime);
		context.setRequestTime(requestTime);

		// accessKey
		String accessKey = getHeaderValue(request, headers, CustomizeHeaderKeyEnum.ACCESS_KEY);
		log.debug("accessKey:{}", accessKey);
		context.setAccessKey(accessKey);

		// sign
		String sign = getHeaderValue(request, headers, CustomizeHeaderKeyEnum.SIGN);
		log.debug("sign:{}", sign);
		context.setSign(sign);

		// debugKey
		String debugKey = getHeaderValue(request, headers, CustomizeHeaderKeyEnum.DEBUG_KEY);
		log.debug("debugKey:{}", debugKey);
		context.setDebugKey(debugKey);

		// 请求路径
		LinkedHashSet<URI> attribute = ((LinkedHashSet<URI>) exchange
				.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR));
		Optional<URI> option = attribute.stream().findFirst();
		if (option.isPresent())
			context.setOriginalRequestPath(option.get().getPath());
		String method = request.getMethodValue();
		context.setMethod(method);
		RequestPath path = request.getPath();
		context.setApiPath(path.value());

		// getParameter
		// String requestParameter = (String) exchange
		// .getAttribute(CacheRequestBodyGlobalFilter.CACHE_PARAMETER_STRING_KEY);
		// context.setParameterString(requestParameter);

		Mono<Void> mono = chain.filter(exchange);
		return mono;
	}

	// private String getParameter(ServerHttpRequest request) {
	// String method = request.getMethodValue();
	// String paramString = "";
	// if ("get".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)) {
	// MultiValueMap<String, String> queryParams = request.getQueryParams();
	// Map<String, String> params = new HashMap<>();
	// for (String parameterName : queryParams.keySet()) {
	// List<String> valueList = queryParams.get(parameterName);
	// String parameterValue = null;
	// if (CollectionUtils.isNotEmpty(valueList)) {
	// parameterValue = String.join(",", valueList);
	// }
	// params.put(parameterName, parameterValue);
	// }
	// paramString = JsonObjectUtil.toJson(params);
	// } else {
	// HttpHeaders headers = request.getHeaders();
	// MediaType contentType = headers.getContentType();
	// AtomicReference<String> bodyRef = new AtomicReference<>();
	// if (MediaType.APPLICATION_JSON.getType().equals(contentType.getType())) {
	// request.getBody().subscribe(buffer -> {
	// CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
	// DataBufferUtils.release(buffer);
	// bodyRef.set(charBuffer.toString());
	// });
	// }
	//
	// }
	//
	// return paramString;
	// }
}
