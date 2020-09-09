package com.g2rain.business.gateway.shell.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.gateway.shell.bo.RouteDefinitionBo;
import com.g2rain.business.gateway.shell.po.param.RouteDefinitionSelectParam;
import com.g2rain.business.gateway.shell.vo.RouteDefinitionVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RequestMapping("route_definition")
@Controller
public class RouteDefinitionController {

	@Autowired
	private RouteDefinitionBo routeDefinitionBo;

	@ApiOperation(notes = "resultData为新增或者修改数据的主键", value = "添加或者更新路由配置")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public SpecificResult<String> addOrUpdate(@RequestBody @Valid RouteDefinitionVo routeDefinition) {
		String routeDefinitionId = routeDefinitionBo.addOrUpdate(routeDefinition);

		SpecificResult<String> successResult = new SpecificResult<String>(BaseResult.SUCCESS);
		successResult.setResultData(routeDefinitionId);

		return successResult;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{routeDefinitionId}")
	@ResponseBody
	public BaseResult delete(@PathVariable("routeDefinitionId") String routeDefinitionId) {
		int deleteRowCount = routeDefinitionBo.delete(routeDefinitionId);

		SpecificResult<Integer> successResult = new SpecificResult<Integer>(BaseResult.SUCCESS);
		successResult.setResultData(deleteRowCount);

		return successResult;
	}

	@ApiOperation(notes = "resultData为带有分页的数据信息", value = "获取路由配置列表")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public SpecificResult<SpecificPageInfoResult<RouteDefinitionVo>> selectByParam(RouteDefinitionSelectParam param) {
		SpecificPageInfoResult<RouteDefinitionVo> routeDefinitionPageInfoResult = routeDefinitionBo
				.selectByParam(param);

		SpecificResult<SpecificPageInfoResult<RouteDefinitionVo>> successResult = new SpecificResult<SpecificPageInfoResult<RouteDefinitionVo>>(
				BaseResult.SUCCESS);
		successResult.setResultData(routeDefinitionPageInfoResult);

		return successResult;
	}
}