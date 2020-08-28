package com.g2rain.business.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.po.OrganSelectParam;


@Mapper
public interface OrganMapper {
	public int insert(OrganPo po);

	public List<OrganPo> selectByParam(OrganSelectParam param);

	public int countByParam(OrganSelectParam param);

	public OrganPo get(String organId);

	public int update(OrganPo organ);

	@Update("update ORGAN set STATUS=#{status} where ORGAN_ID=#{organId}")
	public int updateStatus(@Param("organId") String organId, @Param("status") String status);

	@Select("select * from ORGAN where OUT_ORGAN_ID = #{outOrganId}")
    OrganPo getByOutOrganId(String outOrganId);

	@Delete("update ORGAN set delete_flag = 1 where organ_id = #{organId}")
	public int delete(@Param("organId") String organId);
}
