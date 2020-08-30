package com.g2rain.business.gateway.route;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


	@Service
	public class RouteDefinitionWriter implements RouteDefinitionRepository {
	private Map<String, RouteDefinition> routeDefinitions = new HashMap<>();
	  
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
	    Collection<RouteDefinition> values = this.routeDefinitions.values();
	    return Flux.fromIterable(values);
	}
	  
	  @Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
	    return route.flatMap(routeDefinition -> {
	          this.routeDefinitions.put(routeDefinition.getId(), routeDefinition);
	          return Mono.empty();
	        });
	}
	  
	  @Override
	public Mono<Void> delete(Mono<String> routeId) {
	    return routeId.flatMap(id -> {
	          this.routeDefinitions.remove(id);
	          return Mono.empty();
	        });
	}
}
