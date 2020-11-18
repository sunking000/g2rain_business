package com.g2rain.business.file.store.bo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.file.store.bo.domain.DownloadFileDomain;
import com.g2rain.business.file.store.vo.FileObjectVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReadFileBo {

	@Autowired
	private FileObjectBo fileObjectBo;
	@Autowired
	private FileBo fileBo;

	public DownloadFileDomain readFile(String fileObjectId) {
		FileObjectVo fileObjectVo = fileObjectBo.get(fileObjectId);
		if (fileObjectVo == null) {
			log.error("文件不存在, fileObjectId:{}", fileObjectId);
			throw new BussinessRuntimeException(ErrorCodeEnum.DATA_NON_EXISTENT);
		}

		DownloadFileDomain downloadFileDomain = new DownloadFileDomain(fileObjectVo,
				fileBo.getFileDir());
		if (StringUtils.isBlank(downloadFileDomain.getFileUrl())
				&& StringUtils.isBlank(downloadFileDomain.getOssUrl())) {
			return null;
		}

		return downloadFileDomain;
	}
}