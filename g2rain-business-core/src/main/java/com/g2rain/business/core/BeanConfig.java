package com.g2rain.business.core;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.g2rain.business.common.bo.GetRequestMappingPathPattern;
import com.g2rain.business.common.bo.ServletPathPatternMatcher;
import com.g2rain.business.common.exception.BussinessRuntimeExceptionFilter;
import com.g2rain.business.common.exception.RequestDataValidateExceptionResolver;
import com.g2rain.business.common.filters.CustomRequestContextFilter;
import com.g2rain.business.common.filters.LogFilter;
import com.g2rain.business.common.filters.ResetRequestResponseFilter;
import com.g2rain.business.common.handler.emptystring.EmptyStringToNullConverter;
import com.g2rain.business.common.interceptor.AutoFillInterceptor;
import com.g2rain.business.common.interceptor.LoginInterceptor;
import com.g2rain.business.common.mybatis.SelectInterceptor;


/**
 *
 * @ClassName BeanConfig
 * @Description Bean 配置
 *
 * @date 2017年7月24日 上午10:41:33
 */
@Configuration
@ImportResource(locations = { "classpath:spring-dao-core.xml" })
public class BeanConfig implements ApplicationContextAware, WebMvcConfigurer {

	private ApplicationContext applicationContext;
	private static final String CONTROLLER_PACKAGE = "com.baiwang.aihardware.core.controller";

	@SuppressWarnings("unchecked")
	@Bean
	public ServletPathPatternMatcher servletPathPatternMatcher() {
		AbstractHandlerMethodMapping<RequestMappingInfo> requestMappingHandlerMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>) this.applicationContext
				.getBean("requestMappingHandlerMapping");
		GetRequestMappingPathPattern getRequestMappingPathPattern = new GetRequestMappingPathPattern(
				requestMappingHandlerMapping, CONTROLLER_PACKAGE);
		return getRequestMappingPathPattern;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LoginInterceptor loginInterceptor = new LoginInterceptor();
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
		AutoFillInterceptor autoFillInterceptor = new AutoFillInterceptor();
		registry.addInterceptor(autoFillInterceptor).addPathPatterns("/**");
	}

	@Bean
	public FilterRegistrationBean<BussinessRuntimeExceptionFilter> exceptionFilterRegistration() {
		FilterRegistrationBean<BussinessRuntimeExceptionFilter> exceptionFilterRegistration = new FilterRegistrationBean<BussinessRuntimeExceptionFilter>();
		BussinessRuntimeExceptionFilter exceptionFilter = new BussinessRuntimeExceptionFilter();
		exceptionFilterRegistration.setFilter(exceptionFilter);
		exceptionFilterRegistration.addUrlPatterns("/*");
		exceptionFilterRegistration.setOrder(Integer.MAX_VALUE);
		return exceptionFilterRegistration;
	}

    @Bean
	public FilterRegistrationBean<ResetRequestResponseFilter> resetRequestResponseFilterRegistration() {
		FilterRegistrationBean<ResetRequestResponseFilter> resetRequestResponseFilterRegistration = new FilterRegistrationBean<ResetRequestResponseFilter>();
		ResetRequestResponseFilter resetRequestResponseFilter = new ResetRequestResponseFilter();
        resetRequestResponseFilterRegistration.setFilter(resetRequestResponseFilter);
        resetRequestResponseFilterRegistration.addUrlPatterns("/*");
		resetRequestResponseFilterRegistration.setOrder(2);
        return resetRequestResponseFilterRegistration;
    }

    @Bean
	public FilterRegistrationBean<LogFilter> logFilterRegistration() {
		FilterRegistrationBean<LogFilter> logFilterRegistration = new FilterRegistrationBean<LogFilter>();
		LogFilter logFilter = new LogFilter();
		logFilter.addExcludePath("/user_auth/login_token");
        logFilterRegistration.setFilter(logFilter);
        logFilterRegistration.addUrlPatterns("/*");
		logFilterRegistration.setOrder(5);
        return logFilterRegistration;
    }

	@Bean
	public CustomRequestContextFilter customRequestContextFilter() {
		return new CustomRequestContextFilter();
	}

	@Bean
	public FilterRegistrationBean<CustomRequestContextFilter> customRequestContextFilterRegistration() {
		FilterRegistrationBean<CustomRequestContextFilter> requestContextFilterRegistration = new FilterRegistrationBean<CustomRequestContextFilter>();
		CustomRequestContextFilter filter = customRequestContextFilter();
		requestContextFilterRegistration.setFilter(filter);
		requestContextFilterRegistration.addUrlPatterns("/*");
		requestContextFilterRegistration.setOrder(1);
		return requestContextFilterRegistration;
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new EmptyStringToNullConverter());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		WebMvcConfigurer.super.configureHandlerExceptionResolvers(exceptionResolvers);
		exceptionResolvers.add(new RequestDataValidateExceptionResolver());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

	@Bean
	public SelectInterceptor holdStorePlugin() {
		SelectInterceptor holdStorePlugin = new SelectInterceptor();
		return holdStorePlugin;
	}
}
