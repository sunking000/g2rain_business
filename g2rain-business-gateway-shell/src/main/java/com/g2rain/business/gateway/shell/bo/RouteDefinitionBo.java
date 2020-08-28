package com.g2rain.business.gateway.shell.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.gateway.shell.mapper.RouteDefinitionMapper;
import com.g2rain.business.gateway.shell.po.RouteDefinitionPo;
import com.g2rain.business.gateway.shell.po.param.RouteDefinitionSelectParam;
import com.g2rain.business.gateway.shell.vo.RouteDefinitionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class RouteDefinitionBo {

	@Autowired
	private RouteDefinitionMapper routeDefinitionMapper;
	@Autowired
	private SequenceBo sequenceBo;

	public String addOrUpdate(RouteDefinitionVo routeDefinition) {
		if (StringUtils.isNotBlank(routeDefinition.getRouteDefinitionId())) {
			RouteDefinitionPo routeDefinitionPo = new RouteDefinitionPo(routeDefinition);
			routeDefinitionMapper.update(routeDefinitionPo);
		} else {
			routeDefinition.setRouteDefinitionId(sequenceBo.getRouteDefinitionId());
			RouteDefinitionPo routeDefinitionPo = new RouteDefinitionPo(routeDefinition);
			routeDefinitionMapper.insert(routeDefinitionPo);
		}

		return routeDefinition.getRouteDefinitionId();
	}

	public int delete(String routeDefinitionId) {
		int deleteRowCount = routeDefinitionMapper.delete(routeDefinitionId);
		return deleteRowCount;
	}

	public SpecificPageInfoResult<RouteDefinitionVo> selectByParam(RouteDefinitionSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<RouteDefinitionPo> poList = routeDefinitionMapper.selectByParam(param);
		PageInfo<RouteDefinitionPo> page = new PageInfo<RouteDefinitionPo>(poList);
		List<RouteDefinitionVo> voList = new ArrayList<RouteDefinitionVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (RouteDefinitionPo item : poList) {
				RouteDefinitionVo vo = new RouteDefinitionVo(item);
				voList.add(vo);
			}
		}

		SpecificPageInfoResult<RouteDefinitionVo> result = new SpecificPageInfoResult<>(page.getPageNum(),
				param.getSize(), page.getTotal(), page.getPages());
		result.setObjects(voList);
		return result;
	}

}
