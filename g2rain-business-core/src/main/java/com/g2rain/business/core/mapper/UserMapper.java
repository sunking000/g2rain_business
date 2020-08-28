package com.g2rain.business.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.common.mybatis.DataOrganIsolation;
import com.g2rain.business.core.po.UserPo;
import com.g2rain.business.core.po.UserQueryParam;
import com.g2rain.business.core.po.UserSelectParam;

@DataOrganIsolation(columnName = "organ_id", containCurrent = true)
@Mapper
public interface UserMapper {
	@Update("update user set delete_flag = true where user_id = #{userId}")
	public int delete(@Param("userId") String userId);

	public int insert(UserPo user);

	public UserPo get(String user);

	@Select("select * from user where ACCESS_KEY = #{accessKey}")
	public UserPo getByAccessKey(@Param("accessKey") String accessKey);

	public List<UserPo> selectByParam(UserSelectParam param);
	
	public Integer countByParam(UserSelectParam param);

	public int update(UserPo user);

	void updateAkSk(UserPo userPo);

	List<UserPo> getByQueryParam(UserQueryParam userQueryParam);
}
