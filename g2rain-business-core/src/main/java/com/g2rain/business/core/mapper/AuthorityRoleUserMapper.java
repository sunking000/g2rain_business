package com.g2rain.business.core.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.AuthorityRoleUserPo;
import com.g2rain.business.core.po.AuthorityRoleUserSelectParam;


@Mapper
public interface AuthorityRoleUserMapper {
	@Insert("insert into AUTHORITY_ROLE_USER(ID, CREATE_TIME, UPDATE_TIME, VERSION, ROLE_ID, USER_ID, ORGAN_ID) "
			+ "	values(0, now(), now(), 0, #{roleId}, #{userId}, #{organId})")
	public int insert(AuthorityRoleUserPo po);

	@Select("select * from AUTHORITY_ROLE_USER where ROLE_ID = #{roleId} and USER_ID = #{userId}")
	public AuthorityRoleUserPo getRoleIdAndUserId(@Param("roleId") String roleId,@Param("userId") String userId);

	@Update("update AUTHORITY_ROLE_USER set DELETE_FLAG = true where ROLE_ID = #{roleId} and USER_ID = #{userId}")
	public int delete(@Param("roleId") String roleId, @Param("userId") String userId);

	public List<AuthorityRoleUserPo> selectByParam(AuthorityRoleUserSelectParam param);

	public List<AuthorityRoleUserPo> selectByRoleIdsAndUserId(@Param("userId") String userId,
			@Param("roleIds") Set<String> roleIds);

	public int insertBatch(@Param("roleUsers") List<AuthorityRoleUserPo> roleUsers);

	public int deleteBatch(@Param("roleId") String roleId, @Param("deleteUserIds") Set<String> deleteUserIds);
}
