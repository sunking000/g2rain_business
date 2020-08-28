package com.g2rain.business.gateway.shell.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.gateway.shell.po.RouteDefinitionPo;
import com.g2rain.business.gateway.shell.po.param.RouteDefinitionSelectParam;

@Mapper
public interface RouteDefinitionMapper {

	@Insert("insert into route_definition(ID, CREATE_TIME, UPDATE_TIME, VERSION, ROUTE_DEFINITION_ID, NAME, ENDPOINT_HOST, CONTEXT, METHOD,"
			+ " PATH, HEADER_PARAMETERS, ENDPOINT_PATH, DESCRIPTION) "
			+ " values(0, now(), now(), 0, #{routeDefinitionId}, #{name}, #{endpointHost}, #{context}, #{method},"
			+ " #{path}, #{headerParameters}, #{endpointPath}, #{description})")
	public int insert(RouteDefinitionPo routeDefinition);

	public int update(RouteDefinitionPo routeDefinition);

	@Update("update route_definition set DELETE_FLAG = true where ROUTE_DEFINITION_ID=#{routeDefinitionId}")
	public int delete(@Param("routeDefinitionId") String routeDefinitionId);
	
	public List<RouteDefinitionPo> selectByParam(RouteDefinitionSelectParam param);

	public int countByParam(RouteDefinitionSelectParam param);
}