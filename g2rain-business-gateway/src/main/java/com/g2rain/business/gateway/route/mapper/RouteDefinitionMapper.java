package com.g2rain.business.gateway.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.g2rain.business.gateway.route.po.RouteDefinitionPo;

@Mapper
public interface RouteDefinitionMapper {
	@Select({ "select * from route_definition where DELETE_FLAG = 0" })
	List<RouteDefinitionPo> selectAll();
}
