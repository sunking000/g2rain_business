package com.g2rain.business.gateway.exception;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;

import com.g2rain.business.gateway.filter.CacheRequestBodyGlobalFilter;
import com.g2rain.business.gateway.rc.CommonContextContainer;
import com.g2rain.business.gateway.rc.Context;
import com.g2rain.business.gateway.utils.DateFormatUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
	@Autowired
	private ErrorCodeMessageBo errorCodeMessageBo;

	public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
		this.messageReaders = messageReaders;
	}

	public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
		this.messageWriters = messageWriters;
	}

	public void setViewResolvers(List<ViewResolver> viewResolvers) {
		this.viewResolvers = viewResolvers;
	}

	public void setResultThreadLocal(ThreadLocal<BaseResult> resultThreadLocal) {
		this.resultThreadLocal = resultThreadLocal;
	}

	private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

	public List<HttpMessageReader<?>> getMessageReaders() {
		return this.messageReaders;
	}

	private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

	public List<HttpMessageWriter<?>> getMessageWriters() {
		return this.messageWriters;
	}

	private List<ViewResolver> viewResolvers = Collections.emptyList();

	public List<ViewResolver> getViewResolvers() {
		return this.viewResolvers;
	}

	private ThreadLocal<BaseResult> resultThreadLocal = new ThreadLocal<>();

	public ThreadLocal<BaseResult> getResultThreadLocal() {
		return this.resultThreadLocal;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		ServerHttpRequest request = exchange.getRequest();
		RequestPath path = request.getPath();
		log.error("path:{}, message:{}", path.value(), ex.getMessage());
		log.error(ex.getMessage(), ex);
		String requestParameter = (String) exchange
				.getAttribute(CacheRequestBodyGlobalFilter.CACHE_PARAMETER_STRING_KEY);
		if (StringUtils.isNotBlank(requestParameter))
			log.error("requestParameter:{}", requestParameter);
		// 请求路径
		Context context = CommonContextContainer.getContext(exchange);

		CommonContextContainer.getContext(exchange);
		BaseResult baseResult = null;
		if (ex instanceof BussinessRuntimeException) {
			BussinessRuntimeException bussinessRuntimeException = (BussinessRuntimeException) ex;
			if (bussinessRuntimeException.getLocale() == null)
				bussinessRuntimeException.setLocale(context.getLocale());
			baseResult = bussinessRuntimeException.convertResult();
		} else {
			baseResult = new BaseResult(BaseResult.FAIL);
		}
		baseResult.setRequestId(context.getRequestId());
		baseResult.setTimestamp(DateFormatUtil.format(new Date()));
		baseResult.setPath(context.getApiPath());
		baseResult.setMessage(errorCodeMessageBo.getErrorMessage(baseResult.getErrorCode()));
		this.resultThreadLocal.set(baseResult);
		ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
				.switchIfEmpty(Mono.error(ex)).flatMap(handler -> handler.handle(newRequest))
				.flatMap(response -> write(exchange, response));
	}

	private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
		exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
		return response.writeTo(exchange, new ServerResponse.Context() {
			@Override
			public List<ViewResolver> viewResolvers() {
				return GlobalExceptionHandler.this.getViewResolvers();
			}

			@Override
			public List<HttpMessageWriter<?>> messageWriters() {
				return GlobalExceptionHandler.this.getMessageWriters();
			}
		});
	}

	private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		BaseResult result = this.resultThreadLocal.get();
		return ServerResponse.status(HttpStatus.valueOf(200))
				.contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(result.toMap()));
	}
}
