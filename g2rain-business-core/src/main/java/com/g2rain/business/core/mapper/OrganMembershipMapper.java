package com.g2rain.business.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.OrganMembershipPo;
import com.g2rain.business.core.po.param.OrganMembershipSelectParam;

@Mapper
public interface OrganMembershipMapper {

	@Insert("insert into organ_membership(ID, CREATE_TIME, UPDATE_TIME, VERSION, PARENT_ORGAN_ID, CHILD_ORGAN_ID, CHILD_ORGAN_TYPE) "
			+ " values(0, now(), now(), 0, #{parentOrganId}, #{childOrganId}, #{childOrganType})")
	public int insert(OrganMembershipPo organMembership);

	@Update("update organ_membership set DELETE_FLAG = false where PARENT_ORGAN_ID=#{parentOrganId} and CHILD_ORGAN_ID=#{childOrganId}")
	public int delete(@Param("parentOrganId") String parentOrganId, @Param("childOrganId") String childOrganId);

	public List<OrganMembershipPo> selectByParam(OrganMembershipSelectParam param);

	public int countByParam(OrganMembershipSelectParam param);
}