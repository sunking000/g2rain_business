package com.g2rain.business.core.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.AuthorityRoleEntityPo;
import com.g2rain.business.core.po.AuthorityRoleEntitySelectParam;


@Mapper
public interface AuthorityRoleEntityMapper {
	@Insert("insert into AUTHORITY_ROLE_ENTITY(ID, CREATE_TIME, UPDATE_TIME, VERSION, ROLE_ID, ORGAN_ID, ENTITY_ID, ENTITY_TYPE) values(0, now(), now(), 0, #{roleId}, #{organId}, #{entityId}, #{entityType})")
	public long insert(AuthorityRoleEntityPo po);

	@Select("select * from AUTHORITY_ROLE_ENTITY where ROLE_ID=#{roleId} and ENTITY_ID=#{entityId}")
	public AuthorityRoleEntityPo get(@Param("roleId") String roleId, @Param("entityId") String entityId);

	@Update("update AUTHORITY_ROLE_ENTITY set DELETE_FLAG = true where ROLE_ID=#{roleId} and ENTITY_ID=#{entityId}")
	public long delete(@Param("roleId") String roleId, @Param("entityId") String entityId);

	public List<AuthorityRoleEntityPo> selectByParam(AuthorityRoleEntitySelectParam param);

	public int deleteBatch(@Param("roleId") String roleId, @Param("entityIds") Set<String> entityIds);

	public int insertBatch(@Param("roleEntitys") List<AuthorityRoleEntityPo> roleEntitys);
}
