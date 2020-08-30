package com.g2rain.business.gateway;

import java.util.function.Predicate;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route("path_route", r -> r.path("/get").uri("http://www.baidu.com"))
				.build();
	}

	@Bean
	public Predicate<Object> testPredicate() {
		return new Predicate<Object>() {

			@Override
			public boolean test(Object t) {
				return true;
			}
		};
	}
}
