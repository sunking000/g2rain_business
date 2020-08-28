package com.g2rain.business.gateway.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwaggerBootstrapUI
@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.baiwang.aihardware.gateway.shell"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Iot平台Gateway配置管理")
				.description("Iot平台gateway 管理控制api")
				.termsOfServiceUrl("http:///iot.baiwang.com").version("1.0").build();
	}

}
