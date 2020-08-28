package com.g2rain.business.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.core.po.AuthorityDictPo;
import com.g2rain.business.core.po.AuthorityDictSelectParam;
import com.g2rain.business.core.po.param.AuthorityRoleDictSelectParam;

@Mapper
public interface AuthorityDictMapper {
	@Insert("insert into AUTHORITY_DICT(ID, CREATE_TIME, UPDATE_TIME, VERSION, DICT_ID, TYPE, NAME, CODE, PARENT_DICT_ID, PAGE_CODE ) values(0, now(), now(), 0, #{dictId}, #{type}, #{name}, #{code}, #{parentDictId}, #{pageCode})")
	public int insert(AuthorityDictPo po);

	@Select("select * from AUTHORITY_DICT where DICT_ID = #{dictId}")
	public AuthorityDictPo get(@Param("dictId") String dictId);

	public int update(AuthorityDictPo po);
	
	@Update("update AUTHORITY_DICT set DELETE_FLAG = true  where DICT_ID = #{dictId}")
	public int delete(@Param("dictId") String dictId);

	public List<AuthorityDictPo> selectByParam(AuthorityDictSelectParam param);

	List<AuthorityDictPo> getByComplexParam(@Param("dictId") String dictId,@Param("code") String code
			,@Param("pageCode") String pageCode);

	List<AuthorityDictPo> selectByRoleDictParam(AuthorityRoleDictSelectParam param);

	@Select("select * from AUTHORITY_DICT where DELETE_FLAG = false")
	public List<AuthorityDictPo> selectAll();
}
