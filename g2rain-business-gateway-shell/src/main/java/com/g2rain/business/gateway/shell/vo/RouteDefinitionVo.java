package com.g2rain.business.gateway.shell.vo;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.gateway.shell.po.RouteDefinitionPo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDefinitionVo extends BaseVo {
	private String routeDefinitionId;
	@NotBlank
	private String name;
	private String description;
	@NotBlank
	private String endpointHost;
	@NotBlank
	private String context;
	@NotBlank
	private String method;
	@NotBlank
	private String appPath;
	private String headerParameters;
	private String endpointPath;

	public RouteDefinitionVo() {
		super();
	}

	public RouteDefinitionVo(RouteDefinitionPo routeDefinition) {
		super(routeDefinition);
		BeanUtils.copyProperties(routeDefinition, this);
		this.appPath = routeDefinition.getPath();
	}
}