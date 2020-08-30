package com.g2rain.business.gateway.route.po;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDefinitionPo {
	private long id;
	private Date createTime;
	private Date updateTime;
	private int version;
	/**
	 * 主键
	 */
	private String routeDefinitionId;
	/**
	 * 路由名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 接入点主机地址
	 */
	private String endpointHost;
	/**
	 * 接入点路径
	 */
	private String endpointPath;
	/**
	 * 上下文路径
	 */
	private String context;
	/**
	 * 接口版本
	 */
	private String apiVersion;
	/**
	 * 接口方法
	 */
	private String method;
	/**
	 * 接口路径
	 */
	private String path;
	/**
	 * header参数表
	 */
	private String headerParameters;
}
