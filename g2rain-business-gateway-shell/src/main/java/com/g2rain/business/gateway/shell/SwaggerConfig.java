package com.g2rain.business.gateway.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean("gateway_shell")
	@Order(value = 1)
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.g2rain.business.gateway.shell"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Grain rain 商业服务基础框架Gateway配置管理")
				.description("谷雨商业gateway 管理控制api")
				.termsOfServiceUrl("http:///www.g2rain.com").version("1.0").build();
	}

}
