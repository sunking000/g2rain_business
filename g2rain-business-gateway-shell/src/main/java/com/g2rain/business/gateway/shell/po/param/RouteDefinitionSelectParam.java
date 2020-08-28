package com.g2rain.business.gateway.shell.po.param;

import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDefinitionSelectParam extends BaseSelectParam {
	private String routeDefinitionId;
	private String name;
	private String endpointHost;
	private String context;
	private String method;
	private String appPath;
	private String endpointPath;

	public RouteDefinitionSelectParam() {
		super();
	}
}