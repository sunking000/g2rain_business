package com.g2rain.business.core.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.AuthorityPermissionRolePo;
import com.g2rain.business.core.po.AuthorityPermissionRoleSelectParam;


@Mapper
public interface AuthorityPermissionRoleMapper {
	@Insert("insert into AUTHORITY_PERMISSION_ROLE(ID, CREATE_TIME, UPDATE_TIME, VERSION, ROLE_ID, PERMISSION_ID, PERMISSION_TYPE, ORGAN_ID) values(0, now(), now(), 0, #{roleId}, #{permissionId}, #{permissionType}, #{organId})")
	public int insert(AuthorityPermissionRolePo po);

	public int insertBatch(@Param("permissionRoles") List<AuthorityPermissionRolePo> pos);

	@Select("select * from AUTHORITY_PERMISSION_ROLE where ROLE_ID = #{roleId} and PERMISSION_ID = #{permissionId} ")
	public AuthorityPermissionRolePo get(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

	@Update("update AUTHORITY_PERMISSION_ROLE set DELETE_FLAG = true where ROLE_ID = #{roleId} and PERMISSION_ID = #{permissionId}")
	public int delete(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

	public int deleteBatch(@Param("roleId") String roleId, @Param("permissionIds") Set<String> permissionIds);

	public List<AuthorityPermissionRolePo> selectByParam(AuthorityPermissionRoleSelectParam param);
}
