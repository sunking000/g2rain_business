package com.g2rain.business.file.store.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.file.store.adapter.AliyunOssClient;
import com.g2rain.business.file.store.adapter.AliyunOssSignatureResult;
import com.g2rain.business.file.store.enums.FileObjectStatusEnum;
import com.g2rain.business.file.store.enums.FileStoreTypeEnum;
import com.g2rain.business.file.store.mapper.FileObjectMapper;
import com.g2rain.business.file.store.po.FileObjectPo;
import com.g2rain.business.file.store.po.param.OssAddFileObjectParam;
import com.g2rain.business.file.store.vo.OssSignatureResult;

@Service
public class OssBo {
	@Autowired
	private AliyunOssClient aliyunOssClient;
	@Autowired
	private FileObjectMapper fileObjectMapper;
	@Autowired
	private FileObjectBo fileObjectBo;

	public OssSignatureResult signUploadObjectUrl(OssAddFileObjectParam param) {

		FileObjectPo fileObjectPo = createFileObjectPo(param);
		fileObjectPo.setStatus(FileObjectStatusEnum.INIT.name());
		fileObjectMapper.insert(fileObjectPo);

		AliyunOssSignatureResult generatePresignedUrl4Upload = aliyunOssClient.generatePresignedUrl4Upload(fileObjectPo.getFileId());
		return generatePresignedUrl4Upload;
	}

	private FileObjectPo createFileObjectPo(OssAddFileObjectParam param) {
		return fileObjectBo.createFileObjectPo(param.getOrganId(), param.getFileName(), param.getFileType(),
				FileStoreTypeEnum.OSS.name(),
				null);
	}
	
	public String signGetObjectUrl(String fileId) {
		return aliyunOssClient.generatePresignedUrl4Get(fileId);
	}
}
