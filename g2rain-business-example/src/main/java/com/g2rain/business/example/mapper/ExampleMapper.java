package com.g2rain.business.example.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.example.po.ExamplePo;
import com.g2rain.business.example.po.param.ExampleSelectParam;

@Mapper
public interface ExampleMapper {

	@Insert("insert into example(ID, CREATE_TIME, UPDATE_TIME, VERSION, EXAMPLE_ID, NAME, DESCRIPTION) "
			+ " values(0, now(), now(), 0, #{exampleId}, #{name}, #{description})")
	public int insert(ExamplePo example);

	public int insertBatch(@Param("examples") List<ExamplePo> examples);

	public int update(ExamplePo example);

	@Select("select * from example where EXAMPLE_ID=#{exampleId}")
	public ExamplePo get(@Param("exampleId") String exampleId);

	@Update("update example set DELETE_FLAG = true where EXAMPLE_ID=#{exampleId}")
	public int delete(@Param("exampleId") String exampleId);
	
	public int deleteBatch(@Param("exampleIds") Set<String> exampleIds);

	public List<ExamplePo> selectByParam(ExampleSelectParam param);

	public int countByParam(ExampleSelectParam param);
}