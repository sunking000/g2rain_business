package com.g2rain.business.gateway;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.g2rain.business.gateway.route.RouteDefinitionDomain;
import com.g2rain.business.gateway.route.RouteDefinitionReader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

	private RouteDefinitionReader routeDefinitionReader;

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		List<RouteDefinition> routeDefinitions = routeDefinitionReader.getRouteDefinitions();
		Map<String, RouteDefinitionDomain> routeDefinitionDomainMap = routeDefinitionReader
				.getRouteDefinitionDomainMap();
		Set<String> routeNames = new HashSet<>();

		if (CollectionUtils.isNotEmpty(routeDefinitions)) {
			routeDefinitions.forEach(route -> {
				route.getPredicates().stream()
						.filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
						.forEach(predicateDefinition -> {
							RouteDefinitionDomain routeDefinitionDomain = routeDefinitionDomainMap.get(route.getId());
							if (!routeNames.contains(routeDefinitionDomain.getContext())) {
								resources.add(swaggerResource(routeDefinitionDomain.getContext(),
										predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
												.replace("**", "v2/api-docs")));
								routeNames.add(routeDefinitionDomain.getContext());
							}
						});
			});
		}

		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location) {
		log.info("name:{},location:{}", name, location);
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion("2.0");
		return swaggerResource;
	}
}
