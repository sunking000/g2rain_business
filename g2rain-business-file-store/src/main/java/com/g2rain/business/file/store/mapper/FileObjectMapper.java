package com.g2rain.business.file.store.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.g2rain.business.file.store.po.FileObjectPo;
import com.g2rain.business.file.store.po.param.FileObjectSelectParam;

@Mapper
public interface FileObjectMapper {

	@Insert("insert into file_object(ID, CREATE_TIME, UPDATE_TIME, VERSION, FILE_ID, FILE_NAME, MD5, FILE_TYPE, STORE_TYPE, STORE_PATH, DESCRIPTION) "
			+ " values(0, now(), now(), 0, #{fileId}, #{fileName}, #{md5}, #{fileType}, #{storeType}, #{storePath}, #{description})")
	public int insert(FileObjectPo po);

	public int insertBatch(@Param("fileObjects") List<FileObjectPo> pos);

	public int update(FileObjectPo po);

	@Select("select * from file_object where FILE_ID=#{fileId}")
	public FileObjectPo get(@Param("fileId") String fileId);

	@Update("update file_object set DELETE_FLAG = true where FILE_ID=#{fileId}")
	public int delete(@Param("fileId") String fileId);

	public int deleteBatch(@Param("fileIds") Set<String> fileIds);

	public List<FileObjectPo> selectByParam(FileObjectSelectParam param);

	public int countByParam(FileObjectSelectParam param);
}
