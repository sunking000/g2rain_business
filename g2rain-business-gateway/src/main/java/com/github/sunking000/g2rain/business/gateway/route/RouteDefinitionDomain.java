package com.github.sunking000.g2rain.business.gateway.route;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.sunking000.g2rain.business.gateway.rc.CustomizeHeaderKeyEnum;
import com.github.sunking000.g2rain.business.gateway.route.po.RouteDefinitionPo;
import com.github.sunking000.g2rain.business.gateway.utils.JsonObjectUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDefinitionDomain {
	private static final Logger log = LoggerFactory.getLogger(RouteDefinitionDomain.class);

	private Date updateTime;

	private String id;

	private String endpointHost;

	private String endpointPath;

	private String apiVersion;

	private String context;

	private String method;

	private String path;

	private String headerParameters;

	public RouteDefinitionDomain() {
	}

	public RouteDefinitionDomain(RouteDefinitionPo routeDefinitionPo) {
		this.updateTime = routeDefinitionPo.getUpdateTime();
		this.id = routeDefinitionPo.getRouteDefinitionId();
		this.endpointHost = routeDefinitionPo.getEndpointHost();
		this.endpointPath = routeDefinitionPo.getEndpointPath();
		this.apiVersion = routeDefinitionPo.getApiVersion();
		this.context = routeDefinitionPo.getContext();
		this.method = routeDefinitionPo.getMethod();
		this.path = routeDefinitionPo.getPath();
		this.headerParameters = routeDefinitionPo.getHeaderParameters();
	}

	public RouteDefinition toRouteDefinition() {
		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.setId(this.id);
		String[] split = this.path.split("/");
		int i = 0;
		int order = 1000;
		while (i < split.length && !split[i].equals("**")) {
			order--;
			i++;
		}

		routeDefinition.setOrder(order);
		URI uri2 = UriComponentsBuilder.fromUriString(this.endpointHost).build().toUri();
		routeDefinition.setUri(uri2);
		List<PredicateDefinition> predicates = new LinkedList<>();
		PredicateDefinition pathPredicateDefinition = new PredicateDefinition();
		pathPredicateDefinition.setName("Path");
		pathPredicateDefinition.addArg("_genkey_0", "/" + this.context + this.path);
		predicates.add(pathPredicateDefinition);
		if (StringUtils.isNotBlank(this.method) && !"all".equalsIgnoreCase(this.method)) {
			PredicateDefinition methodPredicateDefinition = new PredicateDefinition();
			methodPredicateDefinition.setName("Method");
			methodPredicateDefinition.addArg("_genkey_0", this.method);
			predicates.add(methodPredicateDefinition);
		}
		if (StringUtils.isNotBlank(this.apiVersion)) {
			PredicateDefinition versionPredicateDefinition = new PredicateDefinition();
			versionPredicateDefinition.setName("Header");
			versionPredicateDefinition.addArg("_genkey_0", CustomizeHeaderKeyEnum.API_VERSION.getUpper());
			versionPredicateDefinition.addArg("_genkey_1", this.apiVersion);
			predicates.add(versionPredicateDefinition);
		}
		
		if ("post".equalsIgnoreCase(this.method) || "put".equalsIgnoreCase(this.method)) {
			PredicateDefinition readBodyPredicateDefinition = new PredicateDefinition();
			readBodyPredicateDefinition.setName("ReadBodyPredicateFactory");
			readBodyPredicateDefinition.addArg("inClass", "#{T(String)}");
			readBodyPredicateDefinition.addArg("predicate", "#{@testPredicate}");
			predicates.add(readBodyPredicateDefinition);
		}

		routeDefinition.setPredicates(predicates);
		List<FilterDefinition> filters = new ArrayList<>();
		FilterDefinition filterDefinition = new FilterDefinition();
		filterDefinition.setName("StripPrefix");
		filterDefinition.addArg("parts", "1");
		filters.add(filterDefinition);
		if (StringUtils.isNotBlank(this.endpointPath) && !"/**".equals(this.path)) {
			FilterDefinition rewriteFilterDefinition = new FilterDefinition();
			rewriteFilterDefinition.setName("RewritePath");
			rewriteFilterDefinition.addArg("regexp", this.path);
			rewriteFilterDefinition.addArg("replacement", this.endpointPath);
			filters.add(rewriteFilterDefinition);
		}
		if (StringUtils.isNotBlank(this.headerParameters)) {
			String[] strings = this.headerParameters.split(";");
			for (String entry : strings) {
				String[] kvArray = entry.split("=");
				if (ArrayUtils.isNotEmpty(kvArray) && kvArray.length == 2) {
					FilterDefinition addHeaderParameterFilter = new FilterDefinition();
					addHeaderParameterFilter.setName("AddRequestHeader");
					addHeaderParameterFilter.addArg("_genkey_0", kvArray[0]);
					addHeaderParameterFilter.addArg("_genkey_1", kvArray[1]);
					filters.add(addHeaderParameterFilter);
				}
			}
		}
		routeDefinition.setFilters(filters);

		log.info("routeDefinition:{}", JsonObjectUtil.toJson(routeDefinition));
		return routeDefinition;
	}
}
