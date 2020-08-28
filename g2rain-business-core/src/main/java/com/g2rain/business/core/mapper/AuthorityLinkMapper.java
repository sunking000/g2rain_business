package com.g2rain.business.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.AuthorityLinkPo;
import com.g2rain.business.core.po.AuthorityLinkSelectParam;
import com.g2rain.business.core.po.AuthorityRoleLinkSelectParam;


@Mapper
public interface AuthorityLinkMapper {
	@Insert("insert into AUTHORITY_LINK(ID, CREATE_TIME, UPDATE_TIME, VERSION, LINK_ID, TYPE, NAME, CODE, LINK_PATH, METHOD, SORT,PARENT_LINK_ID, ICON) "
			+ "values(0, now(), now(), 0, #{linkId}, #{type}, #{name}, #{code}, #{linkPath}, #{method}, #{sort},#{parentLinkId}, #{icon})")
	public long insert(AuthorityLinkPo po);

	@Select("select * from AUTHORITY_LINK where LINK_ID = #{linkId}")
	public AuthorityLinkPo getByLinkId(@Param("linkId") String linkId);

	@Update("update AUTHORITY_LINK set delete_flag = true where LINK_ID = #{linkId}")
	public int delete(@Param("linkId") String linkId);

	public int update(AuthorityLinkPo po);

	public List<AuthorityLinkPo> selectByParam(AuthorityLinkSelectParam param);

	// TODO 需要优化，创建jdbc链接也很慢
	public List<AuthorityLinkPo> selectByAuthorityRoleLinkParam(AuthorityRoleLinkSelectParam param);
}
