package com.g2rain.business.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.po.AuthorityRoleSelectParam;


@Mapper
public interface AuthorityRoleMapper {
	@Insert("insert into AUTHORITY_ROLE(ID, CREATE_TIME, UPDATE_TIME, VERSION, ROLE_ID, ORGAN_ID, CODE, TYPE, NAME) values(0, now(), now(), 0, #{roleId}, #{organId}, #{code}, #{type}, #{name})")
	public int insert(AuthorityRolePo po);

	@Select("select * from AUTHORITY_ROLE where ROLE_ID = #{roleId}")
	public AuthorityRolePo getRoleId(@Param("roleId") String roleId);

	@Update("update AUTHORITY_ROLE set DELETE_FLAG=true where ROLE_ID = #{roleId}")
	public int delete(@Param("roleId") String roleId);

	public List<AuthorityRolePo> getByRoleIds(List<String> roleIds);

	public int update(AuthorityRolePo po);

	@Select("select * from AUTHORITY_ROLE where CODE = #{code}")
	public AuthorityRolePo getByCode(String code);

	public List<AuthorityRolePo> selectByParam(AuthorityRoleSelectParam param);

	public int countByParam(AuthorityRoleSelectParam param);
}
