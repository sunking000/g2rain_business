package com.g2rain.business.file.store.bo.domain;

import com.g2rain.business.common.utils.MediaTypeUtil;
import com.g2rain.business.file.store.enums.DownloadFileDomainActionEnum;
import com.g2rain.business.file.store.enums.FileStoreTypeEnum;
import com.g2rain.business.file.store.vo.FileObjectVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadFileDomain {
	private DownloadFileDomainActionEnum action;
	private String fileName;
	private String ossUrl;
	private String fileUrl;
	private String contentType;

	public DownloadFileDomain() {
		super();
	}

	public DownloadFileDomain(FileObjectVo fileObjectVo, String fileStoreDir) {

		if (FileStoreTypeEnum.NATIVE.name().equals(fileObjectVo.getStoreType())) {
			this.fileUrl = fileStoreDir + "/" + fileObjectVo.getStorePath();
		}

		this.fileName = fileObjectVo.getFileName();
		this.contentType = MediaTypeUtil.getContentType(fileObjectVo.getFileType());
	}
}
