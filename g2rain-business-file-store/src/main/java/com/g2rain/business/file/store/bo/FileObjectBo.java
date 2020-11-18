package com.g2rain.business.file.store.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.file.store.mapper.FileObjectMapper;
import com.g2rain.business.file.store.po.FileObjectPo;
import com.g2rain.business.file.store.po.param.FileObjectSelectParam;
import com.g2rain.business.file.store.vo.FileObjectVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class FileObjectBo {

	@Autowired
	private FileObjectMapper fileObjectMapper;
	@Autowired
	private SequenceBo sequenceBo;
	@Value("${server.context.path}")
	private String serverContextPath;

	public SpecificPageInfoResult<FileObjectVo> list(FileObjectSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<FileObjectPo> poList = fileObjectMapper.selectByParam(param);
		PageInfo<FileObjectPo> page = new PageInfo<FileObjectPo>(poList);

		List<FileObjectVo> fileObjectVos = new ArrayList<FileObjectVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (FileObjectPo item : poList) {
				FileObjectVo vo = new FileObjectVo(item);
				vo.setServerContextPath(serverContextPath);
				fileObjectVos.add(vo);
			}
		}

		SpecificPageInfoResult<FileObjectVo> specificPageInfoResult = new SpecificPageInfoResult<>(page.getPageNum(),
				param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(fileObjectVos);

		return specificPageInfoResult;
	}

	public int addBatch(List<FileObjectPo> pos) {
		return CollectionUtils.isEmpty(pos) ? 0 : fileObjectMapper.insertBatch(pos);
	}

	public FileObjectVo get(String fileObjectId) {
		if (StringUtils.isBlank(fileObjectId)) {
			return null;
		}

		FileObjectPo fileObjectPo = fileObjectMapper.get(fileObjectId);
		if (fileObjectPo == null) {
			return null;
		}

		FileObjectVo fileObjectVo = new FileObjectVo(fileObjectPo);
		fileObjectVo.setServerContextPath(getServerContextPath());

		return fileObjectVo;
	}

	public FileObjectPo createFileObjectPo(String fileOriginalFileName, String fileType, String md5, String storeType,
			String storePath) {
		FileObjectPo fileObjectPo = new FileObjectPo();
		String fileId = sequenceBo.getDefaultSequenceId();
		fileObjectPo.setFileId(fileId);
		fileObjectPo.setFileName(fileOriginalFileName);
		fileObjectPo.setFileType(fileType);
		fileObjectPo.setMd5(md5);
		fileObjectPo.setStorePath(storePath);
		fileObjectPo.setStoreType(storeType);

		return fileObjectPo;
	}

	public String getServerContextPath() {
		return serverContextPath;
	}
}