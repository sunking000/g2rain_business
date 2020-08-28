package com.g2rain.business.gateway.shell.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.gateway.shell.vo.RouteDefinitionVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDefinitionPo extends BasePo {
	private String routeDefinitionId;
	private String name;
	private String description;
	private String endpointHost;
	private String context;
	private String method;
	private String path;
	private String headerParameters;
	private String endpointPath;

	public RouteDefinitionPo() {
		super();
	}

	public RouteDefinitionPo(RouteDefinitionVo routeDefinition) {
		super();
		BeanUtils.copyProperties(routeDefinition, this);
		this.path = routeDefinition.getAppPath();
	}
}