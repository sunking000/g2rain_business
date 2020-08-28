package com.github.sunking000.g2rain.business.gateway.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import com.github.sunking000.g2rain.business.gateway.exception.GlobalExceptionHandler;

@Configuration
public class ExceptionConfig {
	@Primary
	@Bean
	@Order(-2147483648)
	public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewReObjectProvider,
			ServerCodecConfigurer serverCodecConfigurer) {
		GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
		globalExceptionHandler.setViewResolvers(viewReObjectProvider.getIfAvailable(Collections::emptyList));
		globalExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
		globalExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
		return globalExceptionHandler;
	}
}
